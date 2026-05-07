package utilities.other.mappers;

import beans.UtenteBean;

public class Session {
    private UtenteBean user;

    public Session() {
        this.user = null;
    }

    public UtenteBean getUser(){
        return this.user;
    }

    public void setUser(UtenteBean user){
        this.user = user;
    }
}