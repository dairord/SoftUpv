/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

/**
 *
 * @author davido747
 */
public interface IIterator {
    public boolean hasNext();
    public void next();
    public Object currentObject();
    public void first();
}
