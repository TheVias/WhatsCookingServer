/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whatscookingserver;

import com.application.vias.what_s_cooking.DBSnapshot;
import com.application.vias.what_s_cooking.entity.Ingredient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author andrey
 */
public class WhatsCookingServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic hereServerSocket serverSocket = new ServerSocket(6666);
        ServerSocket serverSocket = null;
        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        ObjectOutputStream os = null;
        ObjectInputStream is = null;
        
        while (true) {
            String request = null;
            
            try {
                serverSocket = new ServerSocket(6666);
                System.out.println("Waiting for client...");
                socket = serverSocket.accept();           
                System.out.println("ONLINE");
                out = socket.getOutputStream();
                in = socket.getInputStream();
                os = new ObjectOutputStream(out);
                is = new ObjectInputStream(in);
            } catch (IOException ex) {
                Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                request = (String)is.readObject();
            } catch (IOException ex) {
                Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (request != null) {
                System.out.println(request);
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("WhatsCookingServerPU");            
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                switch (request) {
                    case "getNewReceipts": {
                        break;
                    }
                    case "getNewIngredients": {
                        List<IngredientHib> ingrsHib = (List<IngredientHib>)em.createQuery("from IngredientHib").getResultList();
                        List<Ingredient> list = new ArrayList<Ingredient>();
                        for (IngredientHib ingredientHib : ingrsHib) {
                            list.add(ingredientHib.convertToIngredient());
                        }
                        try {
                            os.writeObject(list);
                        } catch (IOException ex) {
                            Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }
                    case "updateDB": {
                        try {                          
                            DBSnapshot snapshot = (DBSnapshot)is.readObject();
                            DBSnapshot update = new DBSnapshot();
                            List<IngredientHib> ingrsHib = (List<IngredientHib>)em.createQuery("from IngredientHib").getResultList();
                            List<Ingredient> list = new ArrayList<Ingredient>();
                            int[] mas = new int[ingrsHib.size()];
                            int count = 0;
                            for (IngredientHib ingredientHib : ingrsHib) {
                                list.add(ingredientHib.convertToIngredient());
                                mas[count] = ingredientHib.getId();
                                count++;
                            }
                            update.setIngredient(mas);
                            os.writeObject(update);
                        } catch (IOException ex) {
                            Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }
                }
                em.getTransaction().commit();
                em.close();
                emf.close();
            }
            
            try {
                if (serverSocket != null) serverSocket.close();
                if (socket != null) socket.close();
                if (out != null) out.close();
                if (in != null) in.close();
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException ex) {
                Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(WhatsCookingServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("BYE");
            
        }
    }
    
}
