package fpoly.thuyltph35992.lab5;

public class CompanyModel {
    private String _id;
    private String ten;

    public CompanyModel(String ten) {
        this.ten = ten;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
