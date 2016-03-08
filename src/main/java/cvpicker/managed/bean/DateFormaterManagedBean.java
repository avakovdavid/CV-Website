/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class DateFormaterManagedBean {
    
    public String format(Date date) {
        Objects.requireNonNull(date);
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        return formater.format(date);
    }
    
    public String formatHours(Date date) {
        Objects.requireNonNull(date);
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formater.format(date);
    }
    
}
