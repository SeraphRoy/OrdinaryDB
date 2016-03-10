package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        Type fieldType;

        /**
         * The name of the field
         * */
        String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        return itemAr.iterator();
    }

    private ArrayList<TDItem> itemAr;
    private static final long serialVersionUID = 1L;


    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        itemAr = new ArrayList<TDItem>(typeAr.length);
        for(int i = 0; i < typeAr.length; i++){
            if(i < fieldAr.length)
                itemAr.add(i, new TDItem(typeAr[i], fieldAr[i]));
            else
                itemAr.add(i,new TDItem(typeAr[i], null));
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        itemAr = new ArrayList<TDItem>(typeAr.length);
        for(int i = 0; i < typeAr.length; i++)
            itemAr.add(i, new TDItem(typeAr[i], ""));
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        return itemAr.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        return itemAr.get(i).fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        return itemAr.get(i).fieldType;
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        for(int i = 0; i < itemAr.size(); i++)
            if(itemAr.get(i).fieldName == name)
                return i;
        throw new NoSuchElementException();
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        return 0;
    }
    
    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        Iterator<TDItem> it1 = td1.iterator();
        Iterator<TDItem> it2 = td2.iterator();
        int size = td1.numFields() + td2.numFields();
        Type[] typeAr = new Type[size];
        String[] fieldAr = new String[size];
        int index = 0;
        while(it1.hasNext()){
            TDItem temp = it1.next();
            typeAr[index] = temp.fieldType;
            fieldAr[index] = temp.fieldName;
            index ++;
        }
        while(it2.hasNext()){
            TDItem temp = it2.next();
            typeAr[index] = temp.fieldType;
            fieldAr[index] = temp.fieldName;
            index ++;
        }

        return new TupleDesc(typeAr, fieldAr);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        if(!(o instanceof TupleDesc))
            return false;
        TupleDesc d = (TupleDesc)o;
        if(this.numFields() != d.numFields() || this.getSize() != d.getSize())
            return false;
        Iterator<TDItem> it1 = this.iterator();
        Iterator<TDItem> it2 = d.iterator();
        while(it1.hasNext() && it2.hasNext()){
            TDItem temp1 = it1.next();
            TDItem temp2 = it2.next();
            if(!temp1.fieldType.equals(temp2.fieldType) || !temp1.fieldName.equals(temp2.fieldName))
                return false;
        }
        return true;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        String result = "";
        result += itemAr.get(0).fieldType + "(" + itemAr.get(0).fieldName + ")";
        for(int i = 0; i < itemAr.size(); i++){
            result += "," + itemAr.get(i).fieldType + "(" + itemAr.get(i).fieldName + ")";
        }
        return result;
    }
}
