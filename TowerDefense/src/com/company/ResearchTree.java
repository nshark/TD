package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ResearchTree {
    public String type;
    public Tower tower;
    public ArrayList<ResearchNode> nodes;
    ResearchTree(String type, Tower tower){
        this.tower = tower;
        this.type = type;
        nodes=readInTree(type, this);
    }
    public void draw(game game, graphicalInterface gui){
        for(ResearchNode r : nodes){
            r.draw(gui, game);
        }
    }
    public static ArrayList<ResearchNode> readInTree(String type, ResearchTree rt){
        try {
            File myObj = new File("TowerDefense/src/com/company/ResearchTree");
            Scanner myReader = new Scanner(myObj);
            boolean found = false;
            ArrayList<ResearchNode> rNodes = new ArrayList<>();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (found){
                    if (Objects.equals(data.toCharArray()[0], ':')){
                        String[] parsed = data.split("-");
                        parsed[0] = parsed[0].replace(":","");
                        if (parsed[1].toCharArray()[0] == 'x'){
                            rNodes.add(new ResearchNode(parsed[0], null,
                                    Integer.parseInt(parsed[4]), parsed[5], Double.parseDouble(parsed[6]), rt, Double.parseDouble(parsed[2]), Double.parseDouble(parsed[3])));
                        }
                        else {
                            rNodes.add(new ResearchNode(parsed[0], rNodes.get(Integer.parseInt(parsed[1])),
                                    Integer.parseInt(parsed[4]), parsed[5], Double.parseDouble(parsed[6]), rt, Double.parseDouble(parsed[2]), Double.parseDouble(parsed[3])));
                        }
                    }
                    if (Objects.equals(data.toCharArray()[0], '|')){
                        return rNodes;
                    }
                }
                if(Objects.equals("|"+type, data)){
                    found = true;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
