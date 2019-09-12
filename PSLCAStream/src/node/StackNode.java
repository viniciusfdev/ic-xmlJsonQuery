/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package node;

import java.util.List;
import query.Query;
import query.QueryGroupHash;

/**
 *Parsing Stack Node to process open nodes during
 * parser
 * 
 * @author vinicius franca, evandrino barros
 */
public class StackNode {
    private int nodeId;
    private QueryGroupHash usedQueries;
}
