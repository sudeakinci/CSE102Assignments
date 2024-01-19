import java.util.ArrayList;

public class Assignment02_20220808077{
    public static void main(String[] args) {
        //Author: Sude Akıncı
        //Since: 12.04.2023
    }
}

class Product{
    private long Id;
    private String Name;
    private int Quantity;
    private double Price;

    public Product(long Id, String Name, int Quantity, double Price){
        this.Id = Id;
        this.Name = Name;
        setPrice(Price);
        addToInventory(Quantity);
    }

    public long getId(){return Id;}
    public void setId(long Id){this.Id = Id;}

    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}

    public double getPrice(){return Price;}
    public void setPrice(double Price) throws InvalidPriceException{
        if(Price < 0) throw new InvalidPriceException(Price);
        this.Price = Price;
    }
    public int getQuantity(){return Quantity;}
    public void setQuantity(int Quantity) throws InvalidAmountException{
        if(Quantity < 0) throw new InvalidAmountException(Quantity);
        this.Quantity = Quantity;
    }
    public int remaining(){return Quantity;}

    public int addToInventory(int amount) throws InvalidAmountException{
        if(amount < 0)
           throw new InvalidAmountException(amount);
        else return Quantity += amount;
    }
    public double purchase(int amount) throws InvalidAmountException{
        if(amount < 0) throw new InvalidAmountException(amount);
        else if(amount > Quantity) throw new InvalidAmountException(amount, Quantity);
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

    public FoodProduct(long Id, String Name, int Quantity, double Price, int Calories, boolean Dairy, 
      boolean Peanuts, boolean Eggs, boolean Gluten){
        super(Id, Name, Quantity, Price);
        setCalories(Calories);
        this.Dairy = Dairy;
        this.Eggs = Eggs;
        this.Peanuts = Peanuts;
        this.Gluten = Gluten;
    }

    public int getCalories(){return Calories;}
    public void setCalories(int calories)throws InvalidAmountException{
        if(calories < 0) throw new InvalidAmountException(calories);
        this.Calories = calories;
    }
    
    public boolean containsDairy(){return Dairy;}
    public boolean containsEggs(){return Eggs;}
    public boolean containsPeanuts(){return Peanuts;}
    public boolean containsGluten(){return Gluten;}

}

class CleaningProduct extends Product{
    private boolean Liquid;
    private String WhereToUse;
    
    CleaningProduct(long Id, String Name, int Quantity, double Price, boolean Liquid, String WhereToUse){
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
    private ArrayList<Product> cart; 
    private double totalDue;

    public Customer(String Name){
        this.Name = Name;
        this.cart = new ArrayList<>();
        this.totalDue = 0.0;
    }
    public void addToCart(Product product, int count) throws InvalidAmountException{
        try {
            cart.add(product);
            totalDue += product.purchase(count);
            System.out.println(product.getName() + " - " + product.getPrice() + " x " + count + " = " + totalDue);
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    public String receipt(){
        String output = "";
        double total = 0.0;

        for(Product p : cart){
            int count = 0;
            for(Product q : cart)
               if(p.equals(q)) 
                 count++;
            double price = p.getPrice() * count;
            output += String.format("%s - %2f x %d = %2f\n", p.getName(), p.getPrice(), count, price); 
            total += price;     
        } 
        return output += String.format("Total Due - %.2f", total);
        
    }
    public double getTotalDue(){
        double total = 0.0;
        for(Product p : cart)
            total += p.getPrice();
        return total;
    }

    public double pay(double amount) throws InsufficientFundsException{
        if(amount >= getTotalDue()){
            System.out.println("Thank you");
            cart.clear();
            return amount - getTotalDue();
        }
        else throw new InsufficientFundsException(getTotalDue(), amount);
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

    public double pay(double amount, boolean usePoints){
        if(Points > 0 && usePoints){
            double pDiscount = Math.min(Points, getTotalDue() * 100);
            amount -= pDiscount * 0.01;
            Points -= (int)pDiscount;
            if(amount <= 0 && Points > 0)
               Points += (int)(-amount * 100);
        }
        double change = super.pay(amount);
        if(change >= 0 && usePoints)
           Points += (int)(getTotalDue() - change);
        return change;   
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
    private ArrayList<Product> inventory;
    private ArrayList<ClubCustomer> clubc;

    public Store(String Name, String Website){
        this.Name = Name;
        this.Website = Website;
        inventory = new ArrayList<Product>();
        clubc = new ArrayList<ClubCustomer>();
    }
    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}

    public String getWebsite(){return Website;}
    public void setWebsite(String website){Website = website;}

    public int getInventorySize(){return inventory.size();}

    public void addProduct(Product product){inventory.add(product);}

    public Product getProduct(long ID)throws ProductNotFoundException{
        for(Product product : inventory){
            if(product.getId() == ID) return product;
        }
        throw new ProductNotFoundException(ID);
    }
   
    public Product getProduct(String name) throws ProductNotFoundException {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        for (Product product : inventory){
            if (product.getName().equals(name)) return product;
        }    
        throw new ProductNotFoundException(name);
    }

    public void addCustomer(ClubCustomer customer){
        clubc.add(customer);
    }

    public ClubCustomer getCustomer(String phone)throws CustomerNotFoundException{
        for(ClubCustomer club : clubc){
            if(club.getPhone().equals(phone)) return club;
        }
        throw new CustomerNotFoundException(phone);
    }

    public void removeProduct(long ID)throws ProductNotFoundException{
        boolean remove = inventory.removeIf(p -> p.getId() == ID);     

        if(!remove) throw new ProductNotFoundException(ID);
    }

    public void removeProduct(String name)throws ProductNotFoundException{
        boolean remove = inventory.removeIf(p -> p.getName().equals(name));

        if(!remove) throw new ProductNotFoundException(name);
    }

    public void removeCustomer(String phone)throws CustomerNotFoundException{
        boolean remove = clubc.removeIf(p -> p.getPhone().equals(phone));

        if(!remove) throw new CustomerNotFoundException(phone);
    }
}

class CustomerNotFoundException extends IllegalArgumentException{
    private final String phone;

    CustomerNotFoundException(String phone){
        super("Customer phone could not found: " + phone);
        this.phone = phone;
    }

    @Override
    public String toString(){
        return "CustomerNotFoundException: " + phone;
    }
}

class InsufficientFundsException extends RuntimeException{
    private final double total;
    private final double payment;

    InsufficientFundsException(double total, double payment){
        super("Insufficient funds is " + total + " but only " + payment + " given");
        this.total = total;
        this.payment = payment;
    }

    @Override
    public String toString(){
        return "InsufficientFundsException: " + total + " due, but only " + payment + " given";
    }
}

class InvalidAmountException extends RuntimeException{
    private final int amount;
    private final int quantity;

    InvalidAmountException(int amount){
        super("InvalidAmountException: " + amount);
        this.amount = amount;
        this.quantity = -1;
    }
    InvalidAmountException(int amount, int quantity){
        super("InvalidAmountException: " + amount + " was requested, but only " + quantity + " remaining");
        this.amount = amount;
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        if(quantity == -1) return "InvalidAmountException: " + amount;
        else return "InvalidAmountException: " + amount + " was requested, but only " + quantity + " remaining";
    }
}

class InvalidPriceException extends RuntimeException{
    private final double price;

    InvalidPriceException(double price){
        super("InvalidPriceException: " + price);
        this.price = price;
    }

    @Override
    public String toString(){
        return "InvalidPriceException: " + price;
    }
}

class ProductNotFoundException extends IllegalArgumentException{
    private final long ID;
    private final String name;

    ProductNotFoundException(long ID){
        super("ProductNotFoundException ID : " + ID);
        this.ID = ID;
        name = null;
    }
    ProductNotFoundException(String name){
        super("ProductNotFoundException name : " + name);
        this.name = name;
        ID = 25L;
    }

    @Override
    public String toString(){
        if(name == null) return "ProductNotFoundException: ID -" + ID;
        else return "“ProductNotFoundException: Name -" + name;
    }
}
