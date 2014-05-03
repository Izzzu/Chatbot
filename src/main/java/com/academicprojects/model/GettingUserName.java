package com.academicprojects.model;

public class GettingUserName extends Task {

	@Override
	public String doTask(String answer, User user) {
		String[] introduce = {"mam na imiê ", "jestem ", "nazywam siê ","mam na imie ",
							"nazywam sie ", "zwê siê ", "zwe sie "};
		for (int i=0; i<introduce.length; i++)
		{
			if (answer.toLowerCase().contains(introduce[i])) {
				String [] s = answer.toLowerCase().split(introduce[i]);
				if(s.length>=2) {
					String[] s2 = s[1].split(" ");
					if (s2.length>0) 
					{
							String name = s2[0].substring(0,1).toUpperCase().concat(s2[0].substring(1));
					
							setLevel(State.COMPLETED);
							user.setGender(getGender(name));
							user.setName(name);
							return name;
						}
				}
				else {
					setLevel(State.IN_PROGRESS);
					user.setGender(Gender.NOTKNOWN);
					user.setName("Nieznajomy");
					return "Nieznajomy";
				}
			}
		}
		if (!answer.toLowerCase().contains(" "))
		{
			
			if (answer.length()>=2) {
				setLevel(State.COMPLETED);
				String name = answer.substring(0,1).toUpperCase().concat(answer.substring(1));
				user.setGender(getGender(name));
				user.setName(name);
				return name;
			}
			else 
			{
				setLevel(State.IN_PROGRESS);
				user.setGender(Gender.NOTKNOWN);
				user.setName("Nieznajomy");
				return "Nieznajomy";
			}
			
		}
		else  {
			String [] s = answer.toLowerCase().split(" ");
			
			if(s.length>0 && s[0].length()>=2) {
				String name = s[0].substring(0,1).toUpperCase().concat(s[0].substring(1));
				user.setGender(getGender(name));
				user.setName(name);
				return name;
				
			}
			else {
				setLevel(State.IN_PROGRESS);
				user.setGender(Gender.NOTKNOWN);
				user.setName("Nieznajomy");
				return "Nieznajomy";
			}
		}
		

	}

	private Gender getGender(String name) {
		if (name.charAt(name.length()-1)=='a')
		{
			return Gender.FEMALE;
		}
		else return Gender.MALE;
		
	}

}
