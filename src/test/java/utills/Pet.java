package utills;

public class Pet {
    private int id;
    private String[] category;
    private String name;
    private String[] photoUrls;
    private String status;
    private String[] Tags;


    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String[] category){
        this.category = category;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhotoUrls(String[] photoUrls) {
        this.photoUrls = photoUrls;
    }
    public void setTags(String[] Tags) {
        this.Tags = Tags;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}



