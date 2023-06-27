package de.haevn.v2.api;

public class NetworkApi {
    private static final NetworkApi INSTANCE = new NetworkApi();
    public synchronized static NetworkApi getInstance() {
        return INSTANCE;
    }



    private NetworkApi() {
    }

    public void refresh(){

    }

    public synchronized MythicPlusSeasonalApi getRaiderIO() {
        return null;
    }

}
