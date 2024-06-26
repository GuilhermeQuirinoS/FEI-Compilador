import java.util.List;

public class ParserTraducaoC {
    List<Token> tokens;
    Token token;

    public ParserTraducaoC(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token getNexToken(){
        if (tokens.size() > 0){
            return tokens.remove(0);
        }
        return null;
    }

    private void erro(String regra){

        System.out.println("Regra: " + regra);
        System.out.println("Token inválido: " + token.getLexema());
        System.out.println();
        System.exit(0);
    }

    public void main(){
        token = getNexToken();
        if(bloco()){
            if(token.getLexema().equals("$")){
                System.out.println("Sintaticamente correto");
            }else{
                erro("erro sintático");
            }
        }

    }

    //______________________BLOCO__________________________
    public boolean bloco(){
        if (token.getTipo().equals("ID")) {
            if (atribui() && bloco()) {
                return true;
            }
            
        }
        else if ((token.getTipo().equals("INT") || token.getTipo().equals("FLOAT") || token.getTipo().equals("STRING") || token.getTipo().equals("BOOLEAN")  )) {
            if (declara() && bloco()) {
                return true;
            }
        }
        else if (token.getLexema().equals("reditus")){
            if (reditus() && bloco()) {
                return true;
            }
        }
        else if (token.getLexema().equals("propositum")) {
            if (propositum() && bloco()) {
                return true;
            }
        }
        else if (token.getLexema().equals("dicere")) {
            if (dicere() && bloco()) {
                return true;
            }
            
        }else if (token.getLexema().equals("dum")) {
            if (dum() && bloco()) {
                return true;
            }

        }
        else if(token.getTipo().equals("INPUT")){
            if (Input() && bloco()) {
                return true;
            }

        }
        else if (token.getLexema().equals("nintendum")) {
            if (nintendum() && bloco()) {
                return true;
            }

        }
        else if (token.getLexema().equals("si")) {
            if (i_si() && bloco()) {
                return true;
            }

        }else if (token.getTipo().equals("COMENTARIO")) {
            if (noncoment() && bloco()) {
                return true;
            }
            
        }

        return true;
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ERRO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //Ao criar o declara, deu problema com o atribui, então para corrigir o erro eu fiz que para declarar tem que colocar o tipo da 
    //variavel e para atribuir não pode colocar o tipo da variavel

    //____________________Declara_________________________ (TRADUZIDO)
    public boolean declara(){
        if(tipo() && traduz(" ") && matchT("ID",token.getLexema()) && matchT("FIM",";")){
            return true;
        }
        erro("declara");
        return false;
    }

    //??????????????????????????????? String como char LIMITACAO ????????????????????????????
    //??????????????????????????????? Boolean como int LIMITACAO ????????????????????????????
    public boolean tipo(){
        if(matchT("INT","int") || matchT("FLOAT","float") || matchT("STRING","char") || matchT("BOOLEAN","int")){
            return true;
        }
        // erro("veritipo");
        // Vazio
        return false;
    }

    //____________________Atribui__________________________ (TRADUZIDO)
    public boolean atribui(){
        if(matchT("ID", token.getLexema()) && matchT("ATRIBUICAO", "=") && dado() && matchT("FIM", ";")){
            return true;
        }
        erro("atribui");
        return false;
    }

    //&&&&&&&&&&&&&&&&&& Falta poder atribuir tipo boolean &&&&&&&&&&&&&&&&&&&&&&&&&
    public boolean dado(){
        if(matchT("FRASE", token.getLexema()) ||  expre()){
            return true;
        }
        erro("result"); 
        return false;
    }

    //_____________ Reditus (Return) _____________ (TRADUZIDO)
    public boolean reditus(){
        if(matchL("reditus","return") && var()){
            return true;
        }
        erro("reditus");
        return false;
    }

    public boolean var(){
        if((matchT("FRASE",token.getLexema()) || matchT("NUM",token.getLexema()) || matchL("inanis","NULL") || matchT("ID",token.getLexema())) && matchL("?",";")){
            return true;
        }
        erro("var");
        return false;
    }

    //______________ Propositum (FOR) _______________
    public boolean propositum(){
        if(matchL("propositum","") && matchL("(","") && atribui() && condição() && matchL("?","") && atualiza() && matchL(")","")
         && matchL("{","") && bloco() && matchL("}","")){
            return true;
        }
        erro("propositum");
        return false;
    }  

    public boolean atualiza(){
        if(matchT("ID","") && matchL("+","") && matchL("+","")){
            return true;
        }
        erro("atualiza");
        return false;
    }

    //__________________Dicere_____________________ (TRADUZIDO)
    public boolean dicere(){
        if(matchL("dicere", "printf") && matchL("(","(") && printado() && matchL(")",")") && matchT("FIM", ";")){
            return true;
        }
        erro("dicere");
        return false;
    }
    
    // x de dicere
    public boolean printado(){
        if( IDSTRING() && multiprintado()){
            // token = getNexToken();
            return true;
        }
        erro("printado");
        return false;
    }


    //?????????????????????????? Modifiquei a string para printar somento um char pela limitação da Linguagem C ?????????????????????????????
    public boolean IDSTRING(){
        if(matchT("ID", "\"%d\","+token.getLexema()) || matchT("FRASE", "\"%c\","+token.getLexema())){
            // token = getNexToken();
            return true;
        }
        erro("IDSTRING");
        return false;
    }

    // y de dicere
    public boolean multiprintado(){
        if((matchT("VIRGULA", ");\nprintf(") && IDSTRING() && multiprintado())){
            // token = getNexToken();
            return true;
        }
        // vazio
        return true;
    }
    

    //_______________Comentario_________________
    public boolean noncoment(){
        if (matchT("COMENTARIO", token.getLexema())){
            return true;
        }
        return false;
    }
    
    //_________________While_________________ (TRADUZIDO)
    public boolean dum(){
        if (matchL("dum", "while") && matchL("(","(") && condição() && matchL(")", ")") && matchL("{", "{") && bloco() && matchL("}", "}")){
            return true;
        }
        erro("dum");
        return false;
    }


    //__________________Expressao______________________ (TRADUZIDO)
    public boolean expre(){
        if (tato() && exp2()){
            return true;
        }
        erro("expre");
        return false;
    }

    public boolean tato(){
        if ( fator() && tato2()){
            return true;
        }
        erro("tato");
        return false;
    }

    public boolean tato2(){
        if(somamenos() && fator() && tato2()){
            return true;
        }
        // vazio
        return true;
    }

    public boolean exp2(){
        if (multidiv() && tato() && exp2()){
            return true;
        }
        // vazio
        return true;
    }

    public boolean fator(){
        if (matchT("ID", token.getLexema()) || matchT("NUM", token.getLexema()) || matchT("FLUTUANTE", token.getLexema()) || matchL("(", "(") && expre() && matchL(")", ")")){
            return true;
        }
        erro("fator");
        return false;
    }

    public boolean somamenos(){
        if (matchL("+", "+") || matchL("-", "-")){
            return true;
        }
        // erro("somamenos");
        return false;
    }

    public boolean multidiv(){
        if (matchL("*", "*") || matchL("/", "/")){
            return true;
        }
        // erro("multidiv");
        return false;
    }
    
    public boolean compara(){
        if(matchL("<", "<") || matchL(">", ">") || matchL("<=","<=") || matchL(">=", ">=") || matchL("<>", "!=") || matchL("<=>", "==")){
            //token = getNexToken();
            return true;
        }
        return false;
    }

    //&&&&&&&&&&&&&&&&&& Falta poder condicionar tipo boolean &&&&&&&&&&&&&&&&&&&&&&&&&
    //______________________Condição________________________ (TRADUZIDO)
    public boolean condição(){
        if(expre() && compara() && expre()){
            // token = getNexToken();
            return true;
        }
        return false;
    }

    public boolean ID_NUM(){
        if(matchT("ID", token.getLexema()) || matchT("NUM", token.getLexema())){
            // token = getNexToken();
            return true;
        }
        return false;
    }

    //???????????????????????????? INPUT só recebe int pela limitação da Linguagem ???????????????????????????????
    //_______ Input ________ (TRADUZIDO)
    public boolean Input(){
        if(matchT("INPUT","scanf") && matchL("(","(") && traduz("\"%d\",&") && matchT("ID",token.getLexema()) && matchL(")",")") && matchL("?", ";")){
            return true;
            
        }

        erro("Erro input");
        return false;
    }

    //________ Switch Case_______
    public boolean nintendum(){
        if(matchL("nintendum","") && matchL("(","") && ID_FRASE_NUM() && matchL(")","") && matchL("{","") && comentario_wii() && wii() && matchL("}","")){
            return true;
        } 
        erro("nintendum");
        return false;
    }

    public boolean comentario_wii(){
        if (matchT("COMENTARIO","")) {
            return true;
        }
        return true;
    }
    public boolean wii(){
        if(matchL("wii","") && ID_FRASE_NUM() && matchL(":","") && bloco() && matchL("confractus","") && matchL("?","") && continuawii()){
            return true;
        }
        erro("wii");
        return false;
    }

    public boolean continuawii(){
        if( comentario_wii() && (matchL("vexillum","") && matchL(":","") && bloco()) || wii()){
            return true;
        }
        erro("y");
        return false;
    }

    public boolean ID_FRASE_NUM(){
        if(matchT("ID","") || matchT("FRASE","") || matchT("NUM","")){
            return true;
        }
        erro("x");
        return false;
    }

    
       
    //_____________if else__________________

    public boolean e_oppositum(){
        if(matchL("oppositum","") && matchL("{","") && bloco() && matchL("}","")){
            return true;
        }
        return false;
    }

    public boolean i_si(){
        if(matchL("si","") && matchL("(","") && condição() && matchL(")","") && matchL("{","") && bloco() && matchL("}","") && addcond()){
            return true;
        }
        return false;
    }

    //y do if - após o primeiro if, possibilita if/else/vazio
    public boolean addcond(){
        if(e_oppositum()){
            return true;
        }
        return true;
    }

    //_____________Compara Lexema______________
    public boolean matchL(String lexema, String code){

        // _____ Código para debug _____
        //  System.out.println("Necessario: " + lexema);
        //  System.out.println("Lexema: " + token.getLexema());
        //  System.out.println("Token: " + token);
        //  System.err.println();
        
        if(token.getLexema().equals(lexema)){
            traduz(code);
            token = getNexToken();
            return true;
        }
        return false;
    }

    //_____________Compara Tipo______________
    public boolean matchT(String tipo, String code){

        // _____ Código para debug _____
        //  System.out.println("Necessario: " + tipo);
        //  System.out.println("Tipo: " + token.getTipo());
        //  System.out.println("Token: " + token);
        //  System.err.println();

        if(token.getTipo().equals(tipo)){
            traduz(code);
            token = getNexToken();
            return true;
        }
        return false;
    }
    
    public boolean traduz(String s){
        //__________Pulando Linha_____________
        if(s.equals(";") || s.equals("}") || s.equals("{")){
            s += "\n";
        }
        

        //__________Tradução Comentario__________
        if(s.contains("|")){
            //??????????? | modificado para ' pela limitação da linguagem C ????????????????
            s = s.replace("|", "\'");
        }
        if(s.contains("noncommento")){
            s = s.replace("noncommento", "//");
            s = s.replace("oblivion", "\n");
        }
        System.out.print(s);
        return true;
    }
}
