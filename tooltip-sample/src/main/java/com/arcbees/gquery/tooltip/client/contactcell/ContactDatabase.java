/**
 * Copyright 2014 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.arcbees.gquery.tooltip.client.contactcell;

import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The data source for contact information used in the sample. This majority of
 * the code below comes from the GWT showcase :
 * http://gwt.google.com/samples/Showcase/Showcase.html#!CwCellList
 */
public class ContactDatabase extends AbstractDataProvider<ContactDatabase.ContactInfo> {
    /**
     * Information about a contact.
     */
    public static class ContactInfo implements Comparable<ContactInfo> {

        public static final ProvidesKey<ContactInfo> KEY_PROVIDER = new ProvidesKey<ContactInfo>() {
            public Object getKey(ContactInfo item) {
                return item == null ? null : item.getId();
            }
        };

        private static int nextId = 0;
        private String address;
        private Date birthday;
        private String firstName;
        private final int id;
        private String lastName;

        public ContactInfo() {
            this.id = nextId;
            nextId++;
        }

        public int compareTo(ContactInfo o) {
            return (o == null || o.firstName == null) ? -1 : -o.firstName.compareTo(firstName);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ContactInfo) {
                return id == ((ContactInfo) o).id;
            }
            return false;
        }

        /**
         * @return the contact's address
         */
        public String getAddress() {
            return address;
        }

        /**
         * @return the contact's birthday
         */
        public Date getBirthday() {
            return birthday;
        }

        /**
         * @return the contact's firstName
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * @return the contact's full name
         */
        public final String getFullName() {
            return firstName + " " + lastName;
        }

        /**
         * @return the unique ID of the contact
         */
        public int getId() {
            return this.id;
        }

        /**
         * @return the contact's lastName
         */
        public String getLastName() {
            return lastName;
        }

        @Override
        public int hashCode() {
            return id;
        }

        /**
         * Set the contact's address.
         *
         * @param address the address
         */
        public void setAddress(String address) {
            this.address = address;
        }

        /**
         * Set the contact's birthday.
         *
         * @param birthday the birthday
         */
        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        /**
         * Set the contact's first name.
         *
         * @param firstName the firstName to set
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        /**
         * Set the contact's last name.
         *
         * @param lastName the lastName to set
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return getFullName();
        }
    }

    private static final String[] FEMALE_FIRST_NAMES = {"Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer",
            "Maria", "Susan", "Margaret", "Dorothy", "Lisa", "Nancy", "Karen", "Betty", "Helen", "Sandra", "Donna",
            "Carol", "Ruth", "Sharon", "Michelle", "Laura", "Sarah", "Kimberly", "Deborah", "Jessica", "Shirley",
            "Cynthia", "Angela", "Melissa", "Brenda", "Amy", "Anna", "Rebecca", "Virginia", "Kathleen", "Pamela",
            "Martha", "Debra", "Amanda", "Stephanie", "Carolyn", "Christine", "Marie", "Janet", "Catherine", "Frances",
            "Ann", "Joyce", "Diane", "Alice", "Julie", "Heather", "Teresa", "Doris", "Gloria", "Evelyn", "Jean",
            "Cheryl", "Mildred", "Katherine", "Joan", "Ashley", "Judith", "Rose", "Janice", "Kelly", "Nicole", "Judy",
            "Christina", "Kathy", "Theresa", "Beverly", "Denise", "Tammy", "Irene", "Jane", "Lori", "Rachel", "Marilyn",
            "Andrea", "Kathryn", "Louise", "Sara", "Anne", "Jacqueline", "Wanda", "Bonnie", "Julia", "Ruby", "Lois",
            "Tina", "Phyllis", "Norma", "Paula", "Diana", "Annie", "Lillian", "Emily", "Robin", "Peggy", "Crystal",
            "Gladys", "Rita", "Dawn", "Connie", "Florence", "Tracy", "Edna", "Tiffany", "Carmen", "Rosa", "Cindy",
            "Grace", "Wendy", "Victoria", "Edith", "Kim", "Sherry", "Sylvia", "Josephine", "Thelma", "Shannon",
            "Sheila", "Ethel", "Ellen", "Elaine", "Marjorie", "Carrie", "Charlotte", "Monica", "Esther", "Pauline",
            "Emma", "Juanita", "Anita", "Rhonda", "Hazel", "Amber", "Eva", "Debbie", "April", "Leslie", "Clara",
            "Lucille", "Jamie", "Joanne", "Eleanor", "Valerie", "Danielle", "Megan", "Alicia", "Suzanne", "Michele",
            "Gail", "Bertha", "Darlene", "Veronica", "Jill", "Erin", "Geraldine", "Lauren", "Cathy", "Joann",
            "Lorraine", "Lynn", "Sally", "Regina", "Erica", "Beatrice", "Dolores", "Bernice", "Audrey", "Yvonne",
            "Annette", "June", "Samantha", "Marion", "Dana", "Stacy", "Ana", "Renee", "Ida", "Vivian", "Roberta",
            "Holly", "Brittany", "Melanie", "Loretta", "Yolanda", "Jeanette", "Laurie", "Katie", "Kristen", "Vanessa",
            "Alma", "Sue", "Elsie", "Beth", "Jeanne"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller",
            "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson",
            "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen", "Young",
            "Hernandez", "King", "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", "Baker", "Gonzalez", "Nelson",
            "Carter", "Mitchell", "Perez", "Roberts", "Turner", "Phillips", "Campbell", "Parker", "Evans", "Edwards",
            "Collins", "Stewart", "Sanchez", "Morris", "Rogers", "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey",
            "Rivera", "Cooper", "Richardson", "Cox", "Howard", "Ward", "Torres", "Peterson", "Gray", "Ramirez", "James",
            "Watson", "Brooks", "Kelly", "Sanders", "Price", "Bennett", "Wood", "Barnes", "Ross", "Henderson",
            "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes", "Flores", "Washington", "Butler",
            "Simmons", "Foster", "Gonzales", "Bryant", "Alexander", "Russell", "Griffin", "Diaz", "Hayes", "Myers",
            "Ford", "Hamilton", "Graham", "Sullivan", "Wallace", "Woods", "Cole", "West", "Jordan", "Owens", "Reynolds",
            "Fisher", "Ellis", "Harrison", "Gibson", "Mcdonald", "Cruz", "Marshall", "Ortiz", "Gomez", "Murray",
            "Freeman", "Wells", "Webb", "Simpson", "Stevens", "Tucker", "Porter", "Hunter", "Hicks", "Crawford",
            "Henry", "Boyd", "Mason", "Morales", "Kennedy", "Warren", "Dixon", "Ramos", "Reyes", "Burns", "Gordon",
            "Shaw", "Holmes", "Rice", "Robertson", "Hunt", "Black", "Daniels", "Palmer", "Mills", "Nichols", "Grant",
            "Knight", "Ferguson", "Rose", "Stone", "Hawkins", "Dunn", "Perkins", "Hudson", "Spencer", "Gardner",
            "Stephens", "Payne", "Pierce", "Berry", "Matthews", "Arnold", "Wagner", "Willis", "Ray", "Watkins",
            "Olson", "Carroll", "Duncan", "Snyder", "Hart", "Cunningham", "Bradley", "Lane", "Andrews", "Ruiz",
            "Harper", "Fox", "Riley", "Armstrong", "Carpenter", "Weaver", "Greene", "Lawrence", "Elliott", "Chavez",
            "Sims", "Austin", "Peters", "Kelley", "Franklin", "Lawson"};
    private static final String[] MALE_FIRST_NAMES = {"James", "John", "Robert", "Michael", "William", "David",
            "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George",
            "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy",
            "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua",
            "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan",
            "Roger", "Joe", "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry", "Gerald", "Keith", "Samuel",
            "Willie", "Ralph", "Lawrence", "Nicholas", "Roy", "Benjamin", "Bruce", "Brandon", "Adam", "Harry", "Fred",
            "Wayne", "Billy", "Steve", "Louis", "Jeremy", "Aaron", "Randy", "Howard", "Eugene", "Carlos", "Russell",
            "Bobby", "Victor", "Martin", "Ernest", "Phillip", "Todd", "Jesse", "Craig", "Alan", "Shawn", "Clarence",
            "Sean", "Philip", "Chris", "Johnny", "Earl", "Jimmy", "Antonio", "Danny", "Bryan", "Tony", "Luis",
            "Mike", "Stanley", "Leonard", "Nathan", "Dale", "Manuel", "Rodney", "Curtis", "Norman", "Allen", "Marvin",
            "Vincent", "Glenn", "Jeffery", "Travis", "Jeff", "Chad", "Jacob", "Lee", "Melvin", "Alfred", "Kyle",
            "Francis", "Bradley", "Jesus", "Herbert", "Frederick", "Ray", "Joel", "Edwin", "Don", "Eddie", "Ricky",
            "Troy", "Randall", "Barry", "Alexander", "Bernard", "Mario", "Leroy", "Francisco", "Marcus", "Micheal",
            "Theodore", "Clifford", "Miguel", "Oscar", "Jay", "Jim", "Tom", "Calvin", "Alex", "Jon", "Ronnie",
            "Bill", "Lloyd", "Tommy", "Leon", "Derek", "Warren", "Darrell", "Jerome", "Floyd", "Leo", "Alvin",
            "Tim", "Wesley", "Gordon", "Dean", "Greg", "Jorge", "Dustin", "Pedro", "Derrick", "Dan", "Lewis",
            "Zachary", "Corey", "Herman", "Maurice", "Vernon", "Roberto", "Clyde", "Glen", "Hector", "Shane",
            "Ricardo", "Sam", "Rick", "Lester", "Brent", "Ramon", "Charlie", "Tyler", "Gilbert", "Gene"};
    private static final String[] STREET_NAMES = {"Peachtree", "First", "Second", "Third", "Fourth", "Fifth",
            "Sixth", "Tenth", "Fourteenth", "Spring", "Techwood", "West Peachtree", "Juniper", "Cypress", "Fowler",
            "Piedmont", "Juniper", "Main", "Central", "Currier", "Courtland", "Williams", "Centennial", "Olympic",
            "Baker", "Highland", "Pryor", "Decatur", "Bell", "Edgewood", "Mitchell", "Forsyth", "Capital"};
    private static final String[] STREET_SUFFIX = {"Street", "Road", "Lane", "Boulevard", "Way", "Parkway", "Circle",
            "Avenue"};

    public static ContactDatabase get() {
       return new ContactDatabase();
    }

    private ListDataProvider<ContactInfo> dataProvider;
    private Map<Integer, ContactInfo> contactInfoById;

    private ContactDatabase() {
        // init data providers list
        dataProvider = new ListDataProvider<ContactInfo>();
        contactInfoById = new HashMap<Integer, ContactInfo>();

        // Generate initial data.
        generateContacts(250);
    }

    public void remove(ContactInfo contactInfo) {
        dataProvider.getList().remove(contactInfo);
    }

    @Override
    public void addDataDisplay(HasData<ContactInfo> display) {
        dataProvider.addDataDisplay(display);
    }

    public ContactInfo queryContactById(Integer id) {
        return contactInfoById.get(id);
    }

    @Override
    protected void onRangeChanged(HasData<ContactInfo> display) {
    }

    private void addContact(ContactInfo contact) {
        List<ContactInfo> contacts = dataProvider.getList();
        // Remove the contact first so we don't add a duplicate.
        contacts.remove(contact);
        contacts.add(contact);

        contactInfoById.put(contact.getId(), contact);
    }

    private void generateContacts(int count) {
        for (int i = 0; i < count; i++) {
            addContact(createContactInfo());
        }
    }

    @SuppressWarnings("deprecation")
    private ContactInfo createContactInfo() {
        // set all created contact in OTHERS category
        ContactInfo contact = new ContactInfo();
        contact.setLastName(nextValue(LAST_NAMES));
        if (Random.nextBoolean()) {
            // Male.
            contact.setFirstName(nextValue(MALE_FIRST_NAMES));
        } else {
            // Female.
            contact.setFirstName(nextValue(FEMALE_FIRST_NAMES));
        }

        // Create a birthday between 20-80 years ago.
        int year = (new Date()).getYear() - 21 - Random.nextInt(61);
        contact.setBirthday(new Date(year, Random.nextInt(12), 1 + Random.nextInt(31)));

        // Create an address.
        int addrNum = 1 + Random.nextInt(999);
        String addrStreet = nextValue(STREET_NAMES);
        String addrSuffix = nextValue(STREET_SUFFIX);
        contact.setAddress(addrNum + " " + addrStreet + " " + addrSuffix);
        return contact;
    }

    private <T> T nextValue(T[] array) {
        return array[Random.nextInt(array.length)];
    }
}
