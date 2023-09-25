/*
Gregory Sin-Chan
40225969
COMP352
Assignment 2
 */

public class ArrayStack<T> {
    private int current;
    private T[] stk;

    public ArrayStack(){
        current=0;//where the stack will be pointing to within the array. The first empty slot
        stk = (T[]) new Object[8];
    }

    public int size(){
        return current;
    }

    public boolean isEmpty(){
        return this.current <= 0;
    }

    //adds an element to the top of a stack
    public void push(T token){
        if(current < stk.length)
            stk[current++] = token;
        else{
            incrementSize();
            stk[current++] = token;
        }
    }

    //returns the top element of the stack and removes it
    public T pop(){
        if(!this.isEmpty()){
            current--;
            T temp = stk[current];
            stk[current] = null;
            return temp;
        }
        return null;
    }

    //returns the top element without removing it
    public T top(){
        //stops an ArrayOutOfBoundException
        if(current==0)
            return stk[current];
        return stk[current-1];
    }

    //Increases the size of the array by a linear increment as per assignment request
    private void incrementSize(){
        T[] temp = (T[]) new Object[stk.length+8];
        for(int i = 0;i<current;i++){
            temp[i] = stk[i];
        }
        stk = temp;
    }

    @Override
    public String toString() {
        String items = "";
        for(int i =0;i<current;i++){

            if(String.valueOf(stk[i]).equals(String.valueOf(Integer.MAX_VALUE)) )
                items+= "True";
            else if (String.valueOf(stk[i]).equals(String.valueOf(Integer.MIN_VALUE)))
                items += "False";
            else
                items += stk[i].toString() + " ";
        }
        return items;
    }
}
