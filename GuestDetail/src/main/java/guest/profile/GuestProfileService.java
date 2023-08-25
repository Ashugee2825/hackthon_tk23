package guest.profile;

import java.util.ArrayList;

import  guest.profile.GuestProfile;

public class GuestProfileService {

	public void displayList(ArrayList<GuestProfile> guestProfileList)
	{
		guestProfileList.forEach((guestProfile) -> print(guestProfile));
	}
	
	public void print(GuestProfile guestProfile)
	{
		guestProfile.displayValues();
	}
	
}
