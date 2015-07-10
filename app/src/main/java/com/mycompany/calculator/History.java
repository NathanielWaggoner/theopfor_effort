package com.mycompany.calculator;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;

public class History{
    
    int capacity;
    Queue<HistoryItem> historyQueue = new LinkedList<HistoryItem>();
    
    History(int c){
        capacity = c;
    }
    
    class HistoryItem{
        
        private String equation;
        private Double answer;
        
        HistoryItem(String e, Double a){
            equation = e;
            answer = a;
        }
        
        public double getAnswer(){
            return answer;
        }
        
        public String getEquation(){
            return equation;
        }
    }
    
    public void addItem(String equation, double answer){
        
        historyQueue.add(new HistoryItem(equation, answer));
        
        if (historyQueue.size() > capacity)
            historyQueue.remove();
        
        System.out.println("Added: " + equation + ", " + answer + ", and the current size is: " + historyQueue.size());
    }
    
    public HistoryItem getItem(int index){
        Iterator<HistoryItem> iterator = historyQueue.iterator();
        
        for (int i = 0; i < index - 1; i++){
            iterator.next();
        }
        
        return iterator.next();
    }
}