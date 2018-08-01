package data.model.enumeration;

public enum VotoAzienda {

    pessimo(1), discreto(2), buono(3), ottimo(4), eccellente(5);
    
    private int voto;
    
    private VotoAzienda(int voto){
        this.voto = voto;
    }
    
    public int getVoto(){
        return this.voto;
    }
    
}