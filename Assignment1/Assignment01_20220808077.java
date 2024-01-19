import java.util.List;
import java.util.ArrayList;

public class Assignment01_20220808077 {
    //Sude Akıncı
    //28.03.2023
    public static void main(String[] args){
        System.out.println("hi");
    }
}
class Product{
    private String Id;
    private String Name;
    private int Quantity;
    private double Price;

    public Product(String Id, String Name, int Quantity, double Price){
        this.Id = Id;
        this.Name = Name;
        this.Price = Price;
        this.Quantity = Quantity;
    }
    public String getId(){return Id;}
    public void setId(String Id){this.Id = Id;}
    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}
    public double getPrice(){return Price;}
    public void setPrice(double Price){this.Price = Price;}
    public int getQuantity(){return Quantity;}
    public void setQuantity(int Quantity){this.Quantity = Quantity;}
    public int remaining(){return Quantity;}
    public int addToInventory(int amount){
        if(amount >= 0)
           return Quantity += amount;
        else return Quantity;
    }
    public double purchase(int amount){
        if(amount < 0 || amount > Quantity) return 0;
        else{
        Quantity -= amount;
        return amount * Price;
        } 
    }
    
    public String toString(){
        return "Product " + Name + " has " + Quantity + " remaining";
    }
    
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        Product other = (Product) o;

        return Math.abs(this.Price - other.Price) < 0.001;
    }
}
class FoodProduct extends Product{
    private int Calories;
    private boolean Dairy, Eggs, Peanuts, Gluten;

    public FoodProduct(String Id, String Name, int Quantity, double Price, int Calories, boolean Dairy, 
      boolean Peanuts, boolean Eggs, boolean Gluten){
        super(Id, Name, Quantity, Price);
        this.Calories = Calories;
        this.Dairy = Dairy;
        this.Eggs = Eggs;
        this.Peanuts = Peanuts;
        this.Gluten = Gluten;
    }

    public int getCalories(){return Calories;}
    public void setCalories(int calories){this.Calories = calories;}
    
    public boolean containsDairy(){return Dairy;}
    public boolean containsEggs(){return Eggs;}
    public boolean containsPeanuts(){return Peanuts;}
    public boolean containsGluten(){return Gluten;}

}
class CleaningProduct extends Product{
    private boolean Liquid;
    private String WhereToUse;
    
    CleaningProduct(String Id, String Name, int Quantity, double Price, boolean Liquid, String WhereToUse){
        super(Id, Name, Quantity, Price);
        this.Liquid = Liquid;
        this.WhereToUse = WhereToUse;
    }

    public String getWhereToUse(){return WhereToUse;}
    public void setWhereToUse(String WhereToUse){this.WhereToUse = WhereToUse;}

    public boolean isLiquid(){return Liquid;}
}
class Customer{
    protected String Name;

    public Customer(String Name){
        this.Name = Name;
    }
    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}
}
class ClubCustomer extends Customer{
    private String Phone;
    private int Points;

    ClubCustomer(String Name, String Phone){
        super(Name);
        this.Phone = Phone;
        Points = 0;
    }
    public String getPhone(){return Phone;}
    public void setPhone(String Phone){this.Phone = Phone;}
    public int getPoints(){return Points;}
    public void addPoints(int Points){
        if(Points < 0)return;
        this.Points += Points;
    }
    public String toString(){
        return Name + " has " + Points + " points.";
    }
}
class Store{
    private String Name;
    private String Website;
    private List<Product> inventory;

    public Store(String Name, String Website){
        this.Name = Name;
        this.Website = Website;
        inventory = new ArrayList<>();
    }
    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}

    public String getWebsite(){return Website;}
    public void setWebsite(String website){Website = website;}

    public int getInventorySize(){return inventory.size();}

    public void addProduct(Product product, int index){
        if(index < 0 || index >= inventory.size()) inventory.add(product);
        else inventory.add(index, product); 
    }
    public void addProduct(Product product){inventory.add(product);}
    public Product getProduct(int index){
        if(index < 0 || index > inventory.size()) return null;
        return inventory.get(index);
    }
    public int getProductIndex(Product p){
        for(int i = 0; i < inventory.size(); i++){
            if(inventory.get(i).equals(p))
               return i;
        }
        return -1;
    }
}    
