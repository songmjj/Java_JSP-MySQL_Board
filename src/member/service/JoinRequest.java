package member.service;

import java.util.Map;
/* JoinRequest클래스(모델)
 * : JoinService클래스 실행시 필요한 데이터를 담는 클래스 */
public class JoinRequest {
	// JoinService클래스 실행시 필요한 데이터 및 get&set메서드
	private String id;
	private String name;
	private String password;
	private String confirmPassword;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/* isPasswordEqualToConfirm()메서드
	 * : 입력한password가 올바른지 확인하는 메서드
	 *   즉, 입력한password가 null인지 여부& 입력한password가  confirmPassword와 같은지 여부를 판단에 true/false를 리턴함*/
	public boolean isPasswordEqualToConfirm() {
		return password != null && password.equals(confirmPassword);
	}

	/* validate()메서드
	 * : 입력한 회원정보(id,name,password,confirmPassword)가 올바른지 확인하는 메서드
	 *   즉, 입력한password가 null인지 여부& 입력한password가  confirmPassword와 같은지 여부를 판단에 true/false를 리턴함*/
	public void validate(Map<String, Boolean> errors) {
		checkEmpty(errors, id, "id");
		checkEmpty(errors, name, "name");
		checkEmpty(errors, password, "password");
		checkEmpty(errors, confirmPassword, "confirmPassword");
		if (!errors.containsKey("confirmPassword")) {
			if (!isPasswordEqualToConfirm()) {
				errors.put("notMatch", Boolean.TRUE);
			}
		}
	}

	/* checkEmpty()메서드
	 * : 입력한 회원정보(id,name,password,confirmPassword)가 올바르면 errors변수에 값을 대입하는 메서드
	 *   예를들어, 입력한password가 null이거나 비어있으면, errors변수에 ("password",TRUE)을 대입함
	 *   즉, 입력한 회원정보가 null이거나 비어있으면, errors변수 생성 → 나중에 에러처리시 사용 */
	private void checkEmpty(Map<String, Boolean> errors, 
			String value, String fieldName) {
		if (value == null || value.isEmpty())
			errors.put(fieldName, Boolean.TRUE);
	}
}
