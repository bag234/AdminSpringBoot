package org.mrbag.AdminSpringBoot.User;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetalisWithConfig implements UserDetailsService{

	Map<String, UserDetails> users = new ConcurrentHashMap<>();
	
	final String path;
	
	private static final String INFO = """
			# - is comment in format file 
			# Format input user: [user_name | login]:[{Paasword encode}password]:[Authorities | Role]
			# Example:
			# test:{noop}test:Admin,View,...
			""";
	
	private static final Logger log = LoggerFactory.getLogger(UserDetalisWithConfig.class);

	private void load(String path) throws IOException {
		users.clear();
		FileSystemResource r = new FileSystemResource(path);
		if (!r.exists() || !r.isReadable()) {
			log.warn("File not found on disk");
			
			r.getFile().createNewFile();
			
			OutputStream out = r.getOutputStream();
			out.write(INFO.getBytes());
			out.flush();
			out.close();
			log.info("Make new file and write simple instruction.");
			return; 
		}
		
		Scanner s = new Scanner(r.getFile());
		while (s.hasNext()) {
			String cur = s.nextLine().trim();
			if (cur.startsWith("#"))
				continue;
			UserApp app = new UserApp(cur.split(":"));
			users.put(app.getUsername(), app);
		}
		s.close();
	}
	
	public Collection<String> getKey(){
		return users.keySet();
	}
	
	synchronized public void update() throws IOException {
		load(path);
	}
	
	public UserDetalisWithConfig(@Value("${app.file.users}") String path) {
		this.path = path;
		log.info("Start load users credital's from file: " + path);
		try {
			load(path);
		} catch (IOException e) {
			log.error("File create error: ", e);
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails u = users.get(username);
		
		if (u == null)
			throw new UsernameNotFoundException("User not be configure");
		
		return u;
	}


}
