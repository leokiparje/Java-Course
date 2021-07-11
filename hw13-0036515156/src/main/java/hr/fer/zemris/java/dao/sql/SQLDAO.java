package hr.fer.zemris.java.dao.sql;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.model.Poll;
import hr.fer.zemris.java.model.PollOption;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author leokiparje
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getAllPolls() {
		
		List<Poll> polls = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement statement = con.prepareStatement("select * from polls")){	
			try(ResultSet rs = statement.executeQuery()){		
				while(rs!=null && rs.next()) {
					Poll poll = new Poll();
					poll.setId(rs.getLong(1));
					poll.setTitle(rs.getString(2));
					poll.setMessage(rs.getString(3));
					polls.add(poll);
				}	
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return polls;
	}

	@Override
	public List<PollOption> getPollOptions(Long id) {
		
		List<PollOption> pollOptions = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement statement = con.prepareStatement("select * from polloptions where pollid="+id)){	
			try(ResultSet rs = statement.executeQuery()){		
				while(rs!=null && rs.next()) {
					PollOption pollOption = new PollOption();
					pollOption.setId(rs.getLong(1));
					pollOption.setOptionTitle(rs.getString(2));
					pollOption.setOptionLink(rs.getString(3));
					pollOption.setPollId(rs.getLong(4));
					pollOption.setVotesCount(rs.getInt(5));
					pollOptions.add(pollOption);
				}	
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return pollOptions;
	}

	@Override
	public String getTitle(Long id) {
		
		Connection con = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement statement = con.prepareStatement("select title from polls where id="+id)){
			try(ResultSet rs = statement.executeQuery()){
				if (rs.next()) {
					return rs.getString(1);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getMessage(Long id) {
		
		Connection con = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement statement = con.prepareStatement("select message from polls where id="+id)){
			try(ResultSet rs = statement.executeQuery()){
				if (rs.next()) {
					return rs.getString(1);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int getVotes(Long optionId) {
		
		Connection con = SQLConnectionProvider.getConnection();
		
		String query = "select votescount from polloptions where id="+optionId;
		
		try(PreparedStatement statement = con.prepareStatement(query)){
			try(ResultSet rs = statement.executeQuery()){
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void incrementVote(Long optionId) {
		
		Connection con = SQLConnectionProvider.getConnection();
		
		String query = "update polloptions set votescount="+(getVotes(optionId)+1)+" where id="+optionId;
		
		try(PreparedStatement statement = con.prepareStatement(query)){
			if (statement.executeUpdate()!=1) {
				throw new RuntimeException("Execute update should return 1 because 1 row was updated by query.");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<PollOption> getHighestVotes(Long id){
		
		List<PollOption> best = new ArrayList<>();
		
		int highestVote = -1;
		
		for (PollOption pollOption : getPollOptions(id)){
			if (pollOption.getVotesCount()>highestVote) {
				best.clear();
				highestVote = pollOption.getVotesCount();
				best.add(pollOption);
			}else if(pollOption.getVotesCount()==highestVote) {
				best.add(pollOption);
			}
		}
		
		return best;
	}
}




























