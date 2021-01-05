package no.brannstrom.Bounty.models;

import java.util.UUID;

public class Bounty {

	private UUID id;

	private UUID bountiedUser;
	
	private UUID setter;
	
	private int amount;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getBountiedUser() {
		return bountiedUser;
	}

	public void setBountiedUser(UUID bountiedUser) {
		this.bountiedUser = bountiedUser;
	}

	public UUID getSetter() {
		return setter;
	}

	public void setSetter(UUID setter) {
		this.setter = setter;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
