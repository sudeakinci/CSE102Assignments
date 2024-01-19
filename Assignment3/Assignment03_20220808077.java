import java.util.ArrayList;
import java.util.List;

public class Assignment03_20220808077{
    //Author: Sude Akıncı
    //Since: 26.05.2023
}

class Product{
    private long Id;
    private String Name;
    private double Price;

    public Product(long Id, String Name, double Price)throws InvalidPriceException{
        if(Price < 0)
           throw new InvalidPriceException(Price);
        this.Id = Id;
        this.Name = Name;
        setPrice(Price);
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
    public String toString(){
        return Id + " - " + Name + " @" + Price;
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
        super(Id, Name, Price);
        setCalories(Calories);
        Quantity = 0;
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
    
    public CleaningProduct(long Id, String Name, int Quantity, double Price, boolean Liquid, String WhereToUse){
        super(Id, Name, Price);
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
    private int Points;

    public Customer(String Name){
        this.Name = Name;
        this.cart = new ArrayList<>();
        this.totalDue = 0.0;
        this.Points = 0;
        
    }
    public void addToCart(Store store, Product product, int count)throws InvalidAmountException, ProductNotFoundException{
        try {
            cart.add(product);
            totalDue = store.purchase(product, count);
            System.out.println(product.getName() + " - " + product.getPrice() + " x " + count + " = " + totalDue);
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ProductNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    public String receipt(Store store){
        String output = store.getName() + " ";
        double total = 0.0;

        try{
        for(Product p : cart){
            int count = 0;
            for(Product q : cart)
               if(p.equals(q)) 
                 count++;
            double price = p.getPrice() * count;
            output += String.format("%d - %s @ %d x %d = %2f\n", p.getId(), p.getName(), p.getPrice(), count, price); 
            total += price;     
        } 
        }catch(StoreNotFoundException e){
            System.out.println("ERROR: " + e.getMessage());
        }
        return output += String.format("Total Due - %.2f", total);

    }

    public double getTotalDue(Store store) throws StoreNotFoundException {
        if (!store.hasCart(this)) {
            throw new StoreNotFoundException("Customer does not have a cart for the store.");
        }

        return totalDue;
    }
    
    public int getPoints(Store store) throws StoreNotFoundException {
        if (store.hasCustomer(this)) {
            throw new StoreNotFoundException("Customer is not in the customer collection for the store.");
        }

        return Points;
    }
    // extra method for paying
    public double pay(double amount) throws InsufficientFundsException{
        if(amount >= totalDue){
            System.out.println("Thank you");
            cart.clear();
            return amount - totalDue;
        }
        else throw new InsufficientFundsException(totalDue, amount);
    }

    public double pay(Store store, double amount, boolean usePoints) throws InsufficientFundsException, StoreNotFoundException{
        if (!store.hasCart(this)) 
            throw new StoreNotFoundException("Customer does not have a cart for the store.");

        if (amount < totalDue) 
            throw new InsufficientFundsException(totalDue, amount);

        if((amount >= totalDue)){
        System.out.println("Thank you!");
        return amount - totalDue;
        }

        if (usePoints && !(store.hasCustomer(this))){
           setPoints(0);
           
        }

        if (usePoints && store.hasCustomer(this)){
            if(Points > 0 && usePoints){
                double pDiscount = Math.min(Points, totalDue * 100);
                amount -= pDiscount * 0.01;
                Points -= (int)pDiscount;
                if(amount <= 0 && Points > 0)
                   Points += (int)(-amount * 100);
            }
        }
        double change = pay(amount);
        if(change >= 0 && usePoints)
            Points += (int)(totalDue - change);
        return change;

}
    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}

      public void setPoints(int points){
        this.Points = points;
      }
}

//ClubCustomer class has been removed

class Store{
    private String Name;
    private String Website;
    private ArrayList<Product> inventory;
    private int Quantity;//removed from product, added store
    private List<Customer> customers;

    public Store(String Name, String Website, int Quantity){
        this.Name = Name;
        this.Website = Website;
        this.Quantity = Quantity;
        inventory = new ArrayList<>();
        customers = new ArrayList<>();
    }
    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}

    public int remaining(){return Quantity;}
    public void setQuantity(int Quantity){this.Quantity = Quantity;}


    public String getWebsite(){return Website;}
    public void setWebsite(String website){Website = website;}

    public int getCount(){
        return inventory.size();
    }

    public int getInventorySize(){return inventory.size();}

    public boolean hasCart(Customer customer) {
        return customers.contains(customer);
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
        customer.setPoints(0);
    }

    public int getProductCount(Product product)throws ProductNotFoundException{
        for(Product p : inventory){
            if(p.equals(product))
               return remaining();
        }
        throw new ProductNotFoundException(product.getName());
    }

    public int getCustomerPoints(Customer customer)throws CustomerNotFoundException{
        for (Customer c : customers) {
            if (c.equals(customer)) {
                return c.getPoints(null);
            }
        }
        throw new CustomerNotFoundException(customer);
    }
    
    public boolean hasCustomer(Customer customer) {
        return customers.contains(customer);
    }

    public Customer getCustomer(Customer customer)throws CustomerNotFoundException{
        for(Customer c : customers){
            if(c.equals(customer)) 
               return c;
        }
        throw new CustomerNotFoundException(customer);
    }

    public void removeProduct(long ID)throws ProductNotFoundException{
        boolean remove = inventory.removeIf(p -> p.getId() == ID);     

        if(!remove) throw new ProductNotFoundException(ID);
    }

    public void removeProduct(String name)throws ProductNotFoundException{
        boolean remove = inventory.removeIf(p -> p.getName().equals(name));

        if(!remove) throw new ProductNotFoundException(name);
    }

    public void removeProduct(Product product) throws ProductNotFoundException {
        boolean removed = inventory.removeIf(p -> p.equals(product));

        if (!removed) throw new ProductNotFoundException(product.getName());
    }

    public void removeCustomer(Customer customer)throws CustomerNotFoundException{
        for (Customer c : customers) {
            if (c.equals(customer)) {
                customers.remove(customer);
            }
        }
        throw new CustomerNotFoundException(customer);
    }

    public void addToInventory(Product product, int amount){
        if(amount < 0)
           throw new InvalidAmountException(amount);

           for (Product p : inventory) {
            if (p.equals(product)) {
                Quantity += amount;
                return;
            }
        }
        inventory.add(product);
    }
     
    public double purchase(Product product, int amount) throws InvalidAmountException, ProductNotFoundException {
        if (amount < 0 || getCount() < amount) {
            throw new InvalidAmountException(amount);
        }

        for (Product p : inventory) {
            if (p.equals(product)) {
                Quantity = inventory.size() - amount;
                return p.getPrice() * amount;
            }
        }

        throw new ProductNotFoundException("Product not found in the store inventory: " + product.getName());
    }
}

class CustomerNotFoundException extends IllegalArgumentException{
    private Customer customer;

    CustomerNotFoundException(Customer customer){
        super("Customer phone could not found: " + customer.getName());
    }

    @Override
    public String toString(){
        return "CustomerNotFoundException: Name " + customer.getClass().getName();
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
    private Product product;

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

    ProductNotFoundException(Product product){
        super("ProductNotFoundException product : " + product);
        this.product = product;
        name = null;
        ID = 25L;
    }

    @Override
    public String toString(){
        return "ProductNotFoundException: ID -" + ID + " Name - " + name + " Product - " + product.getName();
    }
}

class StoreNotFoundException extends IllegalArgumentException{
    private String name;

    StoreNotFoundException(String name){
        super("StoreNotFoundException" + name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "StoreNotFoundException: " + name;
    }
}
