package webservicepulls;

public class csgoBetModel {
	String emailToPay;
	Long betAmount;
	String matchTitle;
	String teamPicked;
	Long matchId;
	Float matchOdds;
	Boolean PaidOut;
	
	
	
	public csgoBetModel( String emailToPay,Long betAmount,String matchTitle,String teamPicked,Long matchId,Float matchOdds,Boolean PaidOut){
		this.emailToPay = emailToPay;
		this.betAmount = betAmount;
		this.matchTitle = matchTitle;
		this.teamPicked = teamPicked;
		this.matchId = matchId;
		this.matchOdds = matchOdds;
		this.PaidOut = PaidOut;
	}
	
	public String getEmailToPay() {
		return emailToPay;
	}



	public void setEmailToPay(String emailToPay) {
		this.emailToPay = emailToPay;
	}



	public Long getBetAmount() {
		return betAmount;
	}



	public void setBetAmount(Long betAmount) {
		this.betAmount = betAmount;
	}



	public String getMatchTitle() {
		return matchTitle;
	}



	public void setMatchTitle(String matchTitle) {
		this.matchTitle = matchTitle;
	}



	public String getTeamPicked() {
		return teamPicked;
	}



	public void setTeamPicked(String teamPicked) {
		this.teamPicked = teamPicked;
	}



	public Long getMatchId() {
		return matchId;
	}



	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}



	public Float getMatchOdds() {
		return matchOdds;
	}



	public void setMatchOdds(Float matchOdds) {
		this.matchOdds = matchOdds;
	}



	public Boolean getPaidOut() {
		return PaidOut;
	}



	public void setPaidOut(Boolean paidOut) {
		PaidOut = paidOut;
	}


}
