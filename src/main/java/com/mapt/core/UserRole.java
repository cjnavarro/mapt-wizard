package com.mapt.core;

public enum UserRole
{
	ADMIN("ADMIN"),
	VIP("VIP"),
	DEFAULT("DEFAULT");
	
	private String role;
	
	UserRole(String role)
	{
		this.role = role;
	}
	
	public static UserRole fromString(String role)
	{
		for(UserRole userRole : values())
		{
			if(userRole.role.equals(role))
			{
				return userRole;
			}
		}
		
		return DEFAULT;
	}
	
}
