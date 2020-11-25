package co.edu.udea.onomastico.payload;


public class LoginRequest {
	
    private String userEmail;
    
    private String password;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}