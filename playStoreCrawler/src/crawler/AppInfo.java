package crawler;

public class AppInfo {
    private String name;
    private String desc;
    private String company;

    public AppInfo(String name, String desc, String company) {
        this.name = name;
        this.desc = desc;
        this.company = company;
    }

    public AppInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle(){
        return getName()+"_"+getDesc()+"("+getCompany()+")";
    }
}
