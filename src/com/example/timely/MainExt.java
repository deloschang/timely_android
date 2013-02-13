package com.example.timely;

import android.content.Context;

/*
* Extension of Application to add Context
*/

public class MainExt extends MainActivity { 
	private static Context context;
	
	public void onCreate(){
		MainExt.context = getApplicationContext();
	}
	
	public static Context getAppContext(){
		return MainExt.context;
	}
}