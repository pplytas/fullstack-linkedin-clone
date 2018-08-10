package server.endpoints.outputmodels;

public class NotificationOutputModel {

	private String message;
	private String refUserEmail;
	private Long refArticleId;

	public NotificationOutputModel() {}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRefUserEmail() {
		return refUserEmail;
	}

	public void setRefUserEmail(String refUserEmail) {
		this.refUserEmail = refUserEmail;
	}

	public Long getRefArticleId() {
		return refArticleId;
	}

	public void setRefArticleId(Long refArticleId) {
		this.refArticleId = refArticleId;
	}
}
