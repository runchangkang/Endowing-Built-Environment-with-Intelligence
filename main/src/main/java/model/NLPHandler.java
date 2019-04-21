package model;

import com.google.cloud.language.v1.*;
import edu.stanford.nlp.util.ArraySet;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class NLPHandler {
    public static boolean isExecutable(TokenNode command, TokenNode userSpeech){
        if(userSpeech==null)return false;
        if(command.getText().equals(userSpeech.getText())){
            if(!command.hasSons()){return true;}
            else{
                for (TokenNode son : command.getSons().values()){
                    if(userSpeech.hasThisSons(son.getText())){
                        if(!son.hasSons())return true;
                        else{
                            for(TokenNode grandson: son.getSons().values()){
                                if(userSpeech.getThisSon(son.getText()).hasThisSons(grandson.getText()))return true;
                            }
                        }
                    }

                }
            }

        }
        return false;
    }
    public static TokenNode parse(String text) throws IOException {
        if(text == null)return null;
        LanguageServiceClient language = LanguageServiceClient.create();
            Document doc = Document.newBuilder()
                    .setContent(text)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();
            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();
            AnalyzeSyntaxResponse response = language.analyzeSyntax(request);
            List<Token> tmpTokenList = response.getTokensList();

            Token root = tmpTokenList
                    .stream()
                    .filter(s -> s.getDependencyEdge().getLabel().equals(DependencyEdge.Label.ROOT))
                    .findFirst()
                    .get();
            TokenNode rootNode = new TokenNode(root);
            int rootIndex = tmpTokenList.indexOf(root);

            Set<Integer> countedIndex = new ArraySet<>();
            countedIndex.add(rootIndex);
            for (int i = 0; i < tmpTokenList.size(); i++) {
                if (i != rootIndex && tmpTokenList.get(i).getDependencyEdge().getHeadTokenIndex() == rootIndex) {
                    TokenNode newNode = new TokenNode(tmpTokenList.get(i));
                    rootNode.addSon(newNode, i);
                    countedIndex.add(i);
                }
            }
            for (int j = 0; j < tmpTokenList.size(); j++) {
                if (!countedIndex.contains(j) && rootNode.getSons().containsKey(tmpTokenList.get(j).getDependencyEdge().getHeadTokenIndex())) {
                    TokenNode newNode = new TokenNode(tmpTokenList.get(j));
                    rootNode.getSons().get(tmpTokenList.get(j).getDependencyEdge().getHeadTokenIndex()).addSon(newNode, j);
                }
            }
            language.shutdown();
     return rootNode;
    }

}
