package model;

/**
 * Armazena a informação de um produto, como o código de barras, a marca, o
 * modelo e o preço atual.
 */
public class ProdutoInfo {
    private String codigoBarras;
    private String marca;
    private String modelo;
    private long precoAtual;

    //construtor

    public ProdutoInfo(String codigoBarras, String marca, String modelo, long precoAtual) {
        this.codigoBarras = checkCodigoBarras(codigoBarras);
        this.marca = checkMarca(marca);
        this.modelo = checkModelo(modelo);
        this.precoAtual = checkPrecoAtual(precoAtual);
    }
    //getters e setters tirei os setters que sao imutaveis

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public long getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(long precoAtual) {
        this.precoAtual = checkPrecoAtual(precoAtual);
    }

    //método para verificar os construtores
    private String checkCodigoBarras(String codigoBarras){
        if (codigoBarras.isBlank()){
			throw new IllegalArgumentException();
		}
		return codigoBarras;
    }
    private String checkMarca(String marca){
        if (marca.isBlank()){
            throw new IllegalArgumentException();  
        }
        return marca;
    }
    private String checkModelo(String modelo){
        if (modelo.isBlank()){
            throw new IllegalArgumentException();  
        }
        return modelo;
    }   
    private long checkPrecoAtual(long precoAtual){
        if (precoAtual < 0){
            throw new IllegalArgumentException();  
        }
        return precoAtual;
    }
    

    //to string pra testar depois
    @Override
    public String toString() {
        return "ProdutoInfo{" +
                "codigoBarras='" + codigoBarras + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", precoAtual=" + precoAtual +
                '}';
    }


}
