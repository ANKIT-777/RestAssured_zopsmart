package utills;

public class Pet {
    private int id;
    private String name;
    private String[] photoUrls;
    private String status;
    private String Tname;

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(int id, String catname){
        this.id = id;
        this.name = catname;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhotoUrls(String[] photoUrls) {
        this.photoUrls = photoUrls;
    }
    public void setTags(int id,String Tname) {
        this.id = id;
        this.Tname = Tname;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}


