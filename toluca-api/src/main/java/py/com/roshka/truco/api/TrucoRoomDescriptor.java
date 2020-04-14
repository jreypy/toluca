package py.com.roshka.truco.api;

public class TrucoRoomDescriptor {
    private String id;
    private String name;

    public TrucoRoomDescriptor() {
    }

    protected TrucoRoomDescriptor(TrucoRoomDescriptor trucoRoomDescriptor) {
        this.id = trucoRoomDescriptor.id;
        this.name = trucoRoomDescriptor.name;
    }

    public TrucoRoomDescriptor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrucoRoomDescriptor descriptor() {
        return new TrucoRoomDescriptor(this.id, this.name);
    }

    @Override
    public String toString() {
        return "TrucoRoomDescriptor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
