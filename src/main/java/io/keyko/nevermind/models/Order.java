package io.keyko.nevermind.models;


import io.keyko.nevermind.models.asset.AssetMetadata;

public class Order {


    //    @JsonProperty
    public String id;

    //    @JsonProperty
    public AssetMetadata name;

    //    @JsonProperty
    public int timeout;

    //    @JsonProperty
    public String pubkey = null;

    //    @JsonProperty
    public String key = null;

    //    @JsonProperty
    public boolean wasPaid = false;

    //    @JsonProperty
    public int status = 0;

    private Order() {
    }

    public Order(String id, AssetMetadata name, int timeout) {
        this.id = id;
        this.name = name;
        this.timeout = timeout;

    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", timeout=" + timeout +
                ", pubkey='" + pubkey + '\'' +
                ", key='" + key + '\'' +
                ", wasPaid=" + wasPaid +
                ", status=" + status +
                '}';
    }
}