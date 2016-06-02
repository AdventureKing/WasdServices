package com.wasdplay.services.backend;

import java.util.List;

public abstract class Gateway<T> {
	public abstract void insertMatches(List<T> matches);
}
