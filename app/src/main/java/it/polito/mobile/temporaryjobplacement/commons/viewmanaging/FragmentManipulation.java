package it.polito.mobile.temporaryjobplacement.commons.viewmanaging;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import it.polito.mobile.temporaryjobplacement.R;

public class FragmentManipulation{ 
	
	
    public static void avvia_fragment(FragmentActivity act_corrente, Fragment fragment,int id_container,Boolean animazione,Boolean aggiungiBackstack)
	  {   
    	 
    	
    		
    	
    	if(!animazione && ! aggiungiBackstack){
	      // Visualizza il fragment.
	      FragmentManager fragmentManager = act_corrente.getSupportFragmentManager();
	      fragmentManager.beginTransaction()
	      .add(id_container, fragment)
	      //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) //Nessuna animazione di default
	      .commitAllowingStateLoss();}//aNZICHè COMMIT (bug del support v4)
          
		 if(!animazione &&  aggiungiBackstack){
		      // Visualizza il fragment.
		      FragmentManager fragmentManager = act_corrente.getSupportFragmentManager();
		      fragmentManager.beginTransaction()
		      .addToBackStack(null) //permette di usare tasto back tra i fragments se ONBACKPRESSED NON è RIDEFINITO
		      .add(id_container, fragment)
		      //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) //Nessuna animazione di default
		      .commitAllowingStateLoss();}//aNZICHè COMMIT
			  
		 /* 
		  if(animazione && !aggiungiBackstack)
		  {
			// Visualizza il fragment.
		      FragmentManager fragmentManager = act_corrente.getSupportFragmentManager();
		      fragmentManager.beginTransaction()
		      .setCustomAnimations(R.anim.nuova,R.anim.vecchia,0,0)
		      .add(id_container, fragment)
		      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		      .commitAllowingStateLoss();}//aNZICHè COMMIT
		  
	  */
	  
		  if(animazione && aggiungiBackstack)
		  {
			  int x1,x2;
			  if(FragmentManipulation.isUpperAndroid3_0()){x1= R.anim.vecchia; x2=R.anim.nuova1;}
			  else{x1=0; x2=0;}
			  // Visualizza il fragment.
		      FragmentManager fragmentManager = act_corrente.getSupportFragmentManager();
		      fragmentManager.beginTransaction()
		      .setCustomAnimations(R.anim.nuova,x1,x2,R.anim.vecchia1)
		      .addToBackStack(null) 
		      .add(id_container, fragment)
		      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		      .commitAllowingStateLoss();}//aNZICHè COMMIT
	  }


	public static boolean  isUpperAndroid3_0() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			// Do something for HoneyComb 3.0 and above versions
			return true;
		} else {
			// do something for phones running an SDK before HoneyComb 3.0
			return false;
		}
	}
	
	
    
    
    
    public static void rimuovi_fragment(FragmentActivity act_corrente, Fragment fragment){
    	 FragmentManager fragmentManager = act_corrente.getSupportFragmentManager();
	      fragmentManager.beginTransaction()
	     .remove(fragment)
	      .commitAllowingStateLoss();}//aNZICHè COMMIT (bug del support v4)
    	
    	
    
    
    
    
    
    
    
    
    
    
    
    
    /**Metodo pubblico utilizzabile se il comportamento del tasto back viene ridefinito e modificato drasticamente*/
    
    public static void torna_indietro(FragmentActivity act_corrente,int fragment_corrente){
    //estraggo dal fragment stack il numero di schermata successive a quella radice
        //act_corrente.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //POP_BACK_STACK_INCLUSIVE Flag for popBackStack(String, int) and popBackStack(int, int): If set, and the name or ID of a back stack entry has been supplied, then all matching entries will be consumed until one that doesn't match is found or the bottom of the stack is reached. Otherwise, all entries up to but not including that entry will be removed.
        
       for(int i=fragment_corrente;i>0;i--){
    	   act_corrente.onBackPressed();//torna indietro di una schermata alla volta
       }
    	
    	
    }

	public static void svuota_fragmentStack(FragmentActivity act_corrente) {
		act_corrente.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //POP_BACK_STACK_INCLUSIVE Flag for popBackStack(String, int) and popBackStack(int, int): If set, and the name or ID of a back stack entry has been supplied, then all matching entries will be consumed until one that doesn't match is found or the bottom of the stack is reached. Otherwise, all entries up to but not including that entry will be removed.
		
	}
}
