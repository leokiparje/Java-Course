package hr.fer.zemris.java.dao;

import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.model.Poll;
import hr.fer.zemris.java.model.PollOption;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author leokiparje
 *
 */
public interface DAO {

	public List<Poll> getAllPolls();
	
	public List<PollOption> getPollOptions(Long id);
	
	public List<PollOption> getHighestVotes(Long id);
	
	public String getTitle(Long id);
	
	public String getMessage(Long id);
	
	public int getVotes(Long id);
	
	public void incrementVote(Long id);
	
}