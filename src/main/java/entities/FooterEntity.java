package entities;

public class FooterEntity extends BaseEntity {
    public String linkOfElement;
    public String itemName;
    public boolean openInNewTab;
    @Override
    public String toString() {
        return " FooterEntity{" +
                "linkOfElement='" + linkOfElement + '\'' +
                ", textOfXpath='" + itemName + '\'' +
                ", openInNewTab=" + openInNewTab +
                '}';
    }


}
