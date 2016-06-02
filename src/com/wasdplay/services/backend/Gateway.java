package com.wasdplay.services.backend;

import java.util.List;

import com.wasdplay.services.game.Match;

public abstract class Gateway<T extends Match> {
	public abstract void insertMatches(List<T> matches);
	public abstract void updateMatches(List<T> matches);
}
