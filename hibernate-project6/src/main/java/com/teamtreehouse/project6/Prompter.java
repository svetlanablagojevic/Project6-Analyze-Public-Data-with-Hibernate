package com.teamtreehouse.project6;


import com.teamtreehouse.project6.model.Country;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Prompter {

    private static final SessionFactory sessionFactory=buildSessionFactory();

    private static SessionFactory buildSessionFactory () {
        //create a StandardServiceRegistry
        final org.hibernate.service.ServiceRegistry registry= new StandardServiceRegistryBuilder().configure().build();

        return new MetadataSources(registry).buildMetadata().buildSessionFactory();

    }

    private BufferedReader bufferedReader;

    public Prompter () {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }


    private static List<Country> fetchAllCountries() {
        //Open a session
        Session session=sessionFactory.openSession();
        //create Criteria
        Criteria criteria=session.createCriteria(Country.class);
        //Get a list of Contact objects according to the Criteria object
        List<Country> countries=criteria.list();
        //Close the session
        session.close();
        return countries;

    }



    private List<Country> getNonNullInternetUsersCountries(){
        return fetchAllCountries()
                .stream()
                .filter(country -> country.getInternetUsers() != null)
                .collect(Collectors.toList());
    }

    private List<Country> getNonNullLiteracyUsersCountries(){
        return fetchAllCountries()
                .stream()
                .filter(country -> country.getAdultLiteracyRate() != null)
                .collect(Collectors.toList());
    }

    /**CORRELATION COEF**/

    // Gets the mean value of the Internet Usage column
    private double getInternetUsageMeanValue(){
        double internetUsageMeanValue = 0.0;
        double counter = 0.0;

        for(Country country : getNonNullInternetUsersCountries()){
            internetUsageMeanValue += country.getInternetUsers();
            counter += 1.0;
        }

        return internetUsageMeanValue / counter;
    }

    // Gets the mean value of the Adult Literacy column
    private double getAdultLiteracyMeanValue(){
        double adultLiteracyMeanValue = 0.0;
        double counter = 0.0;

        for(Country country : getNonNullLiteracyUsersCountries()){
            adultLiteracyMeanValue += country.getAdultLiteracyRate();
            counter += 1.0;
        }

        return adultLiteracyMeanValue / counter;
    }

    private List<Country> getNonNullInternetUsageAndAdultLiteracyRateCountries(){
        return fetchAllCountries()
                .stream()
                .filter(country -> country.getInternetUsers() != null && country.getAdultLiteracyRate() != null)
                .collect(Collectors.toList());
    }



    //This method is used to calculate the correlation coefficient
    private List<Double> getInternetUsageValuesSubtractedByMeanList(){
        List<Double> internetUsageList = new ArrayList<>();

        for(Country country : getNonNullInternetUsageAndAdultLiteracyRateCountries()){
            internetUsageList.add(
                    country.getInternetUsers() -  getInternetUsageMeanValue());
        }

        return internetUsageList;
    }

    // This method is used to calculate the correlation coefficient
    private List<Double> getAdultLiteracyValuesSubtractedByMeanList(){
        List<Double> adultLiteracyList = new ArrayList<>();

        for(Country country : getNonNullInternetUsageAndAdultLiteracyRateCountries()){
            adultLiteracyList.add(
                    country.getAdultLiteracyRate() - getAdultLiteracyMeanValue());
        }

        return adultLiteracyList;
    }

    public double getCorrelationCoefficient(){
        List<Double> internetUsageList = getInternetUsageValuesSubtractedByMeanList();
        List<Double> adultLiteracyList = getAdultLiteracyValuesSubtractedByMeanList();
        Double product = 0.0;
        Double internetUsageSquared = 0.0;
        Double adultLiteracySquared = 0.0;

        for(int i = 0; i < internetUsageList.size();i++){
            product = product + internetUsageList.get(i) * adultLiteracyList.get(i);
            internetUsageSquared = internetUsageSquared + Math.pow(internetUsageList.get(i), 2);
            adultLiteracySquared = adultLiteracySquared + Math.pow(adultLiteracyList.get(i), 2);
        }

        return product / Math.sqrt(internetUsageSquared * adultLiteracySquared);
    }
    /********/


    public void viewAllCountries() {
        String internet=null;
        String internet1=null;
        String literacy=null;
        //Display list of countries
        for (Country c: fetchAllCountries()) {
        System.out.printf("%-40s",c.getName());

        if (c.getInternetUsers()==null) {
            internet="--";
        }
        else {
            internet=String.format("%.2f",c.getInternetUsers());
        }
        System.out.printf("%-40s",internet);

        if(c.getAdultLiteracyRate()==null) {
            literacy="--";
        }
        else {
            literacy=String.format("%.2f",c.getAdultLiteracyRate());
        }
            System.out.printf("%-40s%n",literacy);
         }
    }


    public void viewStatistics(){
        System.out.printf("%n%n1. Country with MAX internet usage %n%n");

        //Find MAX internet usage country
        final Comparator<Country> comp = (c1, c2) -> Double.compare( c1.getInternetUsers(), c2.getInternetUsers());
        Country max_internet =getNonNullInternetUsersCountries().stream()
                .max(comp)
                .get();
        System.out.println("" + max_internet.getName());
        System.out.println("" + String.format("%.2f", max_internet.getInternetUsers()));

        //Find MIN internet usage country
        System.out.printf("%n%n2. Country with MIN internet usage %n%n");
        final Comparator<Country> comp2 = (c1, c2) -> Double.compare( c1.getInternetUsers(), c2.getInternetUsers());
        Country min_internet =getNonNullInternetUsersCountries().stream()
                .min(comp2)
                .get();
        System.out.println("" + min_internet.getName());
        System.out.println("" + String.format("%.2f", min_internet.getInternetUsers()));

        //Find MAX literacy country
        System.out.printf("%n%n3. Country with MAX adult literacy %n%n");
        final Comparator<Country> comp3 = (c1, c2) -> Double.compare( c1.getAdultLiteracyRate(), c2.getAdultLiteracyRate());
        Country max_literacy =getNonNullLiteracyUsersCountries().stream()
                .max(comp3)
                .get();
        System.out.println("" + max_literacy.getName());
        System.out.println("" + String.format("%.2f", max_literacy.getAdultLiteracyRate()));

        //Find MIN literacy country
        System.out.printf("%n%n4. Country with MIN adult literacy %n%n");
        final Comparator<Country> comp4 = (c1, c2) -> Double.compare( c1.getAdultLiteracyRate(), c2.getAdultLiteracyRate());
        Country min_literacy =getNonNullLiteracyUsersCountries().stream()
                .min(comp4)
                .get();
        System.out.println("" + min_literacy.getName());
        System.out.println("" + String.format("%.2f", min_literacy.getAdultLiteracyRate()));

        //CORRELATION coefficient
        System.out.printf("%n%n5. Correlation coefficent (Internet--Literacy) %n%n");
        System.out.println("" + String.format("%.2f", getCorrelationCoefficient()));

    }

    /**add, delete, update**/

    public void addCountry(Country country) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(country);
        session.getTransaction().commit();
        session.close();
    }

    public void deleteCountry(Country country) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(country);
        session.getTransaction().commit();
        session.close();
    }

    private static void updateCountry(Country country) {
        //Open a session
        Session session=sessionFactory.openSession();
        //Begin a transaction
        session.beginTransaction();
        //Use the session to update contact
        session.update(country);
        //Commit the transaction
        session.getTransaction().commit();
        //Close the session
        session.close();

    }
    /**add, delete, update**/

    public Country getCountryByCode(String code) {
        Session session = sessionFactory.openSession();
        Country country = session.get(Country.class, code);
        session.close();
        return country;
    }

    public void promptAddNewCountry() throws IOException{
        System.out.printf("%n%nAdd country code.%n");
        String  code_string = bufferedReader.readLine().toUpperCase();

        int counter=0;

        for (Country c: fetchAllCountries()) {

            if (c.getCode().equals(code_string)){
               counter+=1;
            }
        }

          if (counter==0) {

              if (code_string.length()==3 && code_string.matches("[a-zA-Z]+")){

                  System.out.printf("%n%nAdd country name.%n");
                  String code_name = bufferedReader.readLine().toUpperCase();

                  System.out.printf("%n%nAdd internet users %n");
                  Double internet_rate = Double.parseDouble(bufferedReader.readLine());

                  System.out.printf("%n%nAdd literacy rate %n");
                  Double literacy_rate = Double.parseDouble(bufferedReader.readLine());

                  Country country_add = new Country.CountrytBuilder(code_string, code_name)
                          .withInternetUsers(internet_rate)
                          .withAdultLiteracyRate(literacy_rate)
                          .build();

                  System.out.println(country_add);

                  addCountry(country_add);
              }

              else  {
                  System.out.printf("%nThe country code must contain 3 letters.%n");
              }

          }

            else if (counter==1){
                System.out.printf("%nThe country code must be unique!%n");

            }
    }

    public void run () {

        int choice;
        boolean quit = false;

        while(quit == false) {
            System.out.printf("%n%n1. View data table%n2. View statistics%n3. Add a country%n");
            System.out.printf("4. Delete a country%n5. Update a country%n6. Quit %n%n");
            System.out.printf("Choose:  ");

            try {
                choice = Integer.parseInt(bufferedReader.readLine());

                if (choice>=1 && choice <=6) {
                    switch (choice) {
                        case 1:
                            System.out.printf("%nCountry\t\t\t\t\t\t\t\tInternet Users\t\t\t\t\t\t\tLiteracy%n");
                            viewAllCountries();
                            break;

                        case 2:
                           viewStatistics();
                            break;

                        //add country
                        case 3:
                           promptAddNewCountry();
                            break;
                        case 4:
                            System.out.printf("%n%nAdd country code of the country you want to delete.%n");
                            String  code_string_delete = bufferedReader.readLine();

                            if (code_string_delete.length()>3){
                                System.out.printf("%nThe country code must contain 3 letters.%n");
                                break;
                            }
                            if (!code_string_delete.matches("[a-zA-Z]+")){
                                System.out.printf("%nThe country code must be a 3 letter string.%n");
                                break;
                            }
                            Country country_delete=getCountryByCode(code_string_delete);

                            deleteCountry(country_delete);
                            System.out.println("Country deleted from the database: "+country_delete.getCode()+ ", "+ country_delete.getName());
                            break;

                        case 5:

                            System.out.printf("%n%nAdd country code of the country you want to update.%n");
                            String  code_string_update = bufferedReader.readLine();

                            if (code_string_update.length()>3){
                                System.out.printf("%nThe country code must contain 3 letters.%n");
                                break;
                            }

                            if (!code_string_update.matches("[a-zA-Z]+")){
                                System.out.printf("%nThe country code must be a 3 letter string.%n");
                                break;
                            }

                            Country country_update=getCountryByCode(code_string_update);

                            System.out.printf("%n%nAdd name of the country you want to update.%n");
                            String  new_name = bufferedReader.readLine();
                            country_update.setName(new_name);

                            System.out.printf("%n%nAdd new value for the internet usage of the country you want to update.%n");
                            Double  new_internet = Double.parseDouble(bufferedReader.readLine());
                            country_update.setInternetUsers(new_internet);

                            System.out.printf("%n%nAdd new value for the literacy of the country you want to update.%n");
                            Double  new_literacy = Double.parseDouble(bufferedReader.readLine());
                            country_update.setInternetUsers(new_literacy);



                            updateCountry(country_update);
                            System.out.println("Country updated from the database: "+country_update.getCode()+ ", "+ country_update.getName() +", "+country_update.getInternetUsers()+ ", "+ country_update.getAdultLiteracyRate());

                            break;
                        case 6:
                            System.exit(0);
                }

                }

                else {
                    System.out.printf("%n%nEntered number must be between 1 and 6!%n");
                }

            }

         catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
