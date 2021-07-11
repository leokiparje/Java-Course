package hr.fer.zemris.java.model;

public class PollOption {

	private Long id;
	private String optionTitle;
	private String optionLink;
	private Long pollId;
	private int votesCount;

	public PollOption() {}
	
	public PollOption(Long id, String optionTitle, String optionLink, Long pollId, int votesCount) {
		super();
		this.id = id;
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
		this.pollId = pollId;
		this.votesCount = votesCount;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOptionTitle() {
		return optionTitle;
	}
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}
	public String getOptionLink() {
		return optionLink;
	}
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}
	public Long getPollId() {
		return pollId;
	}
	public void setPollId(Long pollId) {
		this.pollId = pollId;
	}
	public int getVotesCount() {
		return votesCount;
	}
	public void setVotesCount(int votesCount) {
		this.votesCount = votesCount;
	}
}
