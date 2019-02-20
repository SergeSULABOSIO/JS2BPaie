/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack;

/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurEnregistrement {
    
    public abstract void onDone(String message);
    public abstract void onError(String message);
    public abstract void onUploading(String message);
}
