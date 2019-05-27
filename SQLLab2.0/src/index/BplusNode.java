package index;


import java.util.AbstractMap.SimpleEntry; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.Map.Entry; 
 
public class BplusNode  { 
     
    
    protected boolean isLeaf; 
     
   
    protected boolean isRoot; 
 
    
    protected BplusNode parent; 
     
     
    protected BplusNode previous; 
     
     
    protected BplusNode next;     
     
     
    protected List<Entry<Integer, String>> entries; 
     
     
    protected List<BplusNode> children; 
     
    public BplusNode(boolean isLeaf) { 
        this.isLeaf = isLeaf; 
        entries = new ArrayList<Entry<Integer, String>>(); 
         
        if (!isLeaf) { 
            children = new ArrayList<BplusNode>(); 
        } 
    } 
 
    public BplusNode(boolean isLeaf, boolean isRoot) { 
        this(isLeaf); 
        this.isRoot = isRoot; 
    } 
     
    public String  get(Integer key) { 
         
        //�����Ҷ�ӽڵ� 
        if (isLeaf) { 
	        	int low = 0, high = entries.size() - 1, mid;
	            int comp ;
	    		while (low <= high) {
	    			mid = (low + high) / 2;
	    			comp = entries.get(mid).getKey().compareTo(key);
	    			if (comp == 0) {
	    				return entries.get(mid).getValue();
	    			} else if (comp < 0) {
	    				low = mid + 1;
	    			} else {
	    				high = mid - 1;
	    			}
	    		}
            //δ�ҵ���Ҫ��ѯ�Ķ��� 
            return null; 
        }
        //�������Ҷ�ӽڵ� 
        //���keyС�ڽڵ�����ߵ�key���ص�һ���ӽڵ�������� 
        if (key.compareTo(entries.get(0).getKey()) < 0) { 
            return children.get(0).get(key); 
        //���key���ڵ��ڽڵ����ұߵ�key�������һ���ӽڵ�������� 
        }else if (key.compareTo(entries.get(entries.size()-1).getKey()) >= 0) { 
            return children.get(children.size()-1).get(key); 
        //�����ر�key���ǰһ���ӽڵ�������� 
        }else { 
            int low = 0, high = entries.size() - 1, mid= 0;
            int comp ;
        		while (low <= high) {
        			mid = (low + high) / 2;
        			comp = entries.get(mid).getKey().compareTo(key);
        			if (comp == 0) {
        				return children.get(mid+1).get(key); 
        			} else if (comp < 0) {
        				low = mid + 1;
        			} else {
        				high = mid - 1;
        			}
        		}
        		return children.get(low).get(key);
        } 
    } 
     
    public void insertOrUpdate(Integer key, String value, BplusTree tree){ 
        //�����Ҷ�ӽڵ� 
        if (isLeaf){ 
            //����Ҫ���ѣ�ֱ�Ӳ������� 
            if (contains(key) != -1 || entries.size() < tree.getOrder()){ 
                insertOrUpdate(key, value); 
                if(tree.getHeight() == 0){
                		tree.setHeight(1);
                }
                return ;
            }
            	//��Ҫ���� 
            //���ѳ����������ڵ� 
            BplusNode left = new BplusNode(true); 
            BplusNode right = new BplusNode(true); 
            //�������� 
            if (previous != null){ 
                previous.next = left; 
                left.previous = previous ; 
            } 
            if (next != null) { 
                next.previous = right; 
                right.next = next; 
            } 
            if (previous == null){ 
            } 
 
            left.next = right; 
            right.previous = left; 
            previous = null; 
            next = null; 
             
            //����ԭ�ڵ�ؼ��ֵ����ѳ������½ڵ� 
            copy2Nodes(key, value, left, right, tree);
            
            //������Ǹ��ڵ� 
            if (parent != null) { 
                //�������ӽڵ��ϵ 
                int index = parent.children.indexOf(this); 
                parent.children.remove(this); 
                left.parent = parent; 
                right.parent = parent; 
                parent.children.add(index,left); 
                parent.children.add(index + 1, right); 
                parent.entries.add(index,right.entries.get(0));
                entries = null; //ɾ����ǰ�ڵ�Ĺؼ�����Ϣ
                children = null; //ɾ����ǰ�ڵ�ĺ��ӽڵ�����
                 
                //���ڵ�������¹ؼ��� 
                parent.updateInsert(tree); 
                parent = null; //ɾ����ǰ�ڵ�ĸ��ڵ�����
            //����Ǹ��ڵ�     
            }else { 
                isRoot = false; 
                BplusNode parent = new BplusNode (false, true); 
                tree.setRoot(parent); 
                left.parent = parent; 
                right.parent = parent; 
                parent.children.add(left); 
                parent.children.add(right);
                parent.entries.add(right.entries.get(0));
                entries = null; 
                children = null; 
            } 
            return ;
         
        }
        //�������Ҷ�ӽڵ�
        //���keyС�ڵ��ڽڵ�����ߵ�key���ص�һ���ӽڵ�������� 
        if (key.compareTo(entries.get(0).getKey()) < 0) { 
            children.get(0).insertOrUpdate(key, value, tree); 
        //���key���ڽڵ����ұߵ�key�������һ���ӽڵ�������� 
        }else if (key.compareTo(entries.get(entries.size()-1).getKey()) >= 0) { 
            children.get(children.size()-1).insertOrUpdate(key, value, tree); 
        //�����ر�key���ǰһ���ӽڵ�������� 
        }else { 
            int low = 0, high = entries.size() - 1, mid= 0;
            int comp ;
        		while (low <= high) {
        			mid = (low + high) / 2;
        			comp = entries.get(mid).getKey().compareTo(key);
        			if (comp == 0) {
        				children.get(mid+1).insertOrUpdate(key, value, tree);
        				break;
        			} else if (comp < 0) {
        				low = mid + 1;
        			} else {
        				high = mid - 1;
        			}
        		}
        		if(low>high){
        			children.get(low).insertOrUpdate(key, value, tree);
        		}
        } 
    }
 
	private void copy2Nodes(Integer key, String value, BplusNode left,
			BplusNode right,BplusTree tree) {
		//���������ڵ�ؼ��ֳ��� 
        int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2; 
		boolean b = false;//���ڼ�¼��Ԫ���Ƿ��Ѿ�������
		for (int i = 0; i < entries.size(); i++) {
			if(leftSize !=0){
				leftSize --;
				if(!b&&entries.get(i).getKey().compareTo(key) > 0){
					left.entries.add(new SimpleEntry<Integer, String>(key, value));
					b = true;
					i--;
				}else {
					left.entries.add(entries.get(i));
				}
			}else {
				if(!b&&entries.get(i).getKey().compareTo(key) > 0){
					right.entries.add(new SimpleEntry<Integer, String>(key, value));
					b = true;
					i--;
				}else {
					right.entries.add(entries.get(i));
				}
			}
		}
		if(!b){
			right.entries.add(new SimpleEntry<Integer, String>(key, value));
		}
	} 
     
    /** ����ڵ���м�ڵ�ĸ��� */ 
    protected void updateInsert(BplusTree tree){ 
         
        //����ӽڵ�����������������Ҫ���Ѹýڵ�    
        if (children.size() > tree.getOrder()) { 
            //���ѳ����������ڵ� 
            BplusNode left = new BplusNode (false); 
            BplusNode right = new BplusNode (false); 
            //���������ڵ��ӽڵ�ĳ��� 
            int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2; 
            int rightSize = (tree.getOrder() + 1) / 2; 
            //�����ӽڵ㵽���ѳ������½ڵ㣬�����¹ؼ��� 
            for (int i = 0; i < leftSize; i++){ 
                left.children.add(children.get(i)); 
                children.get(i).parent = left; 
            } 
            for (int i = 0; i < rightSize; i++){ 
                right.children.add(children.get(leftSize + i)); 
                children.get(leftSize + i).parent = right; 
            } 
            for (int i = 0; i < leftSize - 1; i++) {
            		left.entries.add(entries.get(i)); 
			}
            for (int i = 0; i < rightSize - 1; i++) {
            		right.entries.add(entries.get(leftSize + i)); 
			}
            
            //������Ǹ��ڵ� 
            if (parent != null) { 
                //�������ӽڵ��ϵ 
                int index = parent.children.indexOf(this); 
                parent.children.remove(this); 
                left.parent = parent; 
                right.parent = parent; 
                parent.children.add(index,left); 
                parent.children.add(index + 1, right); 
                parent.entries.add(index,entries.get(leftSize - 1));
                entries = null; 
                children = null; 
                 
                //���ڵ���¹ؼ��� 
                parent.updateInsert(tree); 
                parent = null; 
            //����Ǹ��ڵ�     
            }else { 
                isRoot = false; 
                BplusNode parent = new BplusNode (false, true); 
                tree.setRoot(parent);
                tree.setHeight(tree.getHeight() + 1);
                left.parent = parent; 
                right.parent = parent; 
                parent.children.add(left); 
                parent.children.add(right); 
                parent.entries.add(entries.get(leftSize - 1));
                entries = null; 
                children = null; 
            } 
        }
    } 
     
    /** ɾ���ڵ���м�ڵ�ĸ���*/ 
    protected void updateRemove(BplusTree tree) { 
 
        // ����ӽڵ���С��M / 2����С��2������Ҫ�ϲ��ڵ� 
        if (children.size() < tree.getOrder() / 2 || children.size() < 2) { 
            if (isRoot) { 
                // ����Ǹ��ڵ㲢���ӽڵ������ڵ���2��OInteger 
                if (children.size() >= 2) return; 
                // �������ӽڵ�ϲ� 
                BplusNode root = children.get(0); 
                tree.setRoot(root); 
                tree.setHeight(tree.getHeight() - 1);
                root.parent = null; 
                root.isRoot = true; 
                entries = null; 
                children = null; 
                return ;
            } 
            //����ǰ��ڵ�  
            int currIdx = parent.children.indexOf(this); 
            int prevIdx = currIdx - 1; 
            int nextIdx = currIdx + 1; 
            BplusNode previous = null;
            BplusNode	next = null; 
            if (prevIdx >= 0) { 
                previous = parent.children.get(prevIdx); 
            } 
            if (nextIdx < parent.children.size()) { 
                next = parent.children.get(nextIdx); 
            } 
             
            // ���ǰ�ڵ��ӽڵ�������M / 2���Ҵ���2������䴦�貹 
            if (previous != null  
                    && previous.children.size() > tree.getOrder() / 2 
                    && previous.children.size() > 2) { 
                //ǰҶ�ӽڵ�ĩβ�ڵ���ӵ���λ 
                int idx = previous.children.size() - 1; 
                BplusNode borrow = previous.children.get(idx); 
                previous.children.remove(idx); 
                borrow.parent = this; 
                children.add(0, borrow);
                int preIndex = parent.children.indexOf(previous);
                
                entries.add(0,parent.entries.get(preIndex));
                parent.entries.set(preIndex, previous.entries.remove(idx - 1));
                return ;
            }
            
            // �����ڵ��ӽڵ�������M / 2���Ҵ���2������䴦�貹
            if (next != null  
                    && next.children.size() > tree.getOrder() / 2 
                    && next.children.size() > 2) { 
                //��Ҷ�ӽڵ���λ��ӵ�ĩβ 
                BplusNode borrow = next.children.get(0); 
                next.children.remove(0); 
                borrow.parent = this; 
                children.add(borrow); 
                int preIndex = parent.children.indexOf(this);
                entries.add(parent.entries.get(preIndex));
                parent.entries.set(preIndex, next.entries.remove(0));
                return ;
            }
            
            // ͬǰ��ڵ�ϲ� 
            if (previous != null  
                    && (previous.children.size() <= tree.getOrder() / 2 
                    || previous.children.size() <= 2)) { 
                for (int i = 0; i < children.size(); i++) {
                    previous.children.add(children.get(i)); 
				}
                for(int i = 0; i < previous.children.size();i++){
                		previous.children.get(i).parent = this;
                }
                int indexPre = parent.children.indexOf(previous);
                previous.entries.add(parent.entries.get(indexPre));
                for (int i = 0; i < entries.size(); i++) {
					previous.entries.add(entries.get(i));
				}
                children = previous.children;
                entries = previous.entries;
                
                //���¸��ڵ�Ĺؼ����б�
                parent.children.remove(previous);
                previous.parent = null;
                previous.children = null;
                previous.entries = null;
                parent.entries.remove(parent.children.indexOf(this));
                if((!parent.isRoot 
                			&& (parent.children.size() >= tree.getOrder() / 2
                			&& parent.children.size() >= 2))
                			||parent.isRoot && parent.children.size() >= 2){
	                 return ;
				}
                parent.updateRemove(tree); 
                return ;
            }   
            
	        // ͬ����ڵ�ϲ� 
            if (next != null  
                    && (next.children.size() <= tree.getOrder() / 2 
                    || next.children.size() <= 2)) { 
                for (int i = 0; i < next.children.size(); i++) { 
                    BplusNode child = next.children.get(i); 
                    children.add(child); 
                    child.parent = this; 
                } 
                int index = parent.children.indexOf(this);
                entries.add(parent.entries.get(index));
                for (int i = 0; i < next.entries.size(); i++) {
					entries.add(next.entries.get(i));
				}
                parent.children.remove(next);
                next.parent = null;
                next.children = null;
                next.entries = null;
                parent.entries.remove(parent.children.indexOf(this));
                if((!parent.isRoot && (parent.children.size() >= tree.getOrder() / 2
                			&& parent.children.size() >= 2))
                			||parent.isRoot && parent.children.size() >= 2){
	                 return ;
				}
                parent.updateRemove(tree); 
                return ;
            } 
        } 
    } 
     
    public String remove(Integer key, BplusTree tree){ 
        //�����Ҷ�ӽڵ� 
        if (isLeaf){ 
            //����������ùؼ��֣���ֱ�ӷ��� 
            if (contains(key) == -1){ 
                return null; 
            } 
            //�������Ҷ�ӽڵ����Ǹ��ڵ㣬ֱ��ɾ�� 
            if (isRoot) { 
            		if(entries.size() == 1){
            			tree.setHeight(0);
            		}
                return remove(key); 
            }
            //����ؼ���������M / 2��ֱ��ɾ�� 
            if (entries.size() > tree.getOrder() / 2 && entries.size() > 2) { 
               return remove(key); 
            }
            //�������ؼ�����С��M / 2������ǰ�ڵ�ؼ���������M / 2������䴦�貹 
            if (previous != null &&  
            		    previous.parent == parent
                    && previous.entries.size() > tree.getOrder() / 2 
                    && previous.entries.size() > 2 ) { 
                //��ӵ���λ 
            		int size = previous.entries.size(); 
                entries.add(0, previous.entries.remove(size - 1)); 
                int index = parent.children.indexOf(previous);
                parent.entries.set(index, entries.get(0));
                return remove(key);
            }
            //�������ؼ�����С��M / 2�����Һ�ڵ�ؼ���������M / 2������䴦�貹 
            if (next != null 
            				&& next.parent == parent 
                        && next.entries.size() > tree.getOrder() / 2 
                        && next.entries.size() > 2) { 
                entries.add(next.entries.remove(0)); 
                int index = parent.children.indexOf(this);
                parent.entries.set(index, next.entries.get(0));
                return remove(key);
            }
            	
            //ͬǰ��ڵ�ϲ� 
            if (previous != null	 
            			&& previous.parent == parent 
                    && (previous.entries.size() <= tree.getOrder() / 2 
                    || previous.entries.size() <= 2)) { 
            		String returnStringalue =  remove(key);
                for (int i = 0; i < entries.size(); i++) { 
                    //����ǰ�ڵ�Ĺؼ�����ӵ�ǰ�ڵ��ĩβ
                		previous.entries.add(entries.get(i));
                } 
                entries = previous.entries;
                parent.children.remove(previous);
                previous.parent = null; 
                previous.entries = null;
                //�������� 
                if (previous.previous != null) { 
                    BplusNode temp = previous; 
                    temp.previous.next = this; 
                    previous = temp.previous; 
                    temp.previous = null; 
                    temp.next = null;                          
                }else { 
                    previous.next = null; 
                    previous = null; 
                }
                parent.entries.remove(parent.children.indexOf(this));
                if((!parent.isRoot && (parent.children.size() >= tree.getOrder() / 2
                			&& parent.children.size() >= 2))
                			||parent.isRoot && parent.children.size() >= 2){
                	 	return returnStringalue;
                }
                parent.updateRemove(tree);
                return returnStringalue;
            }
            //ͬ����ڵ�ϲ�
            if(next != null  
            			&& next.parent == parent
	                && (next.entries.size() <= tree.getOrder() / 2 
	                || next.entries.size() <= 2)) { 
            		String returnStringalue = remove(key); 
	            for (int i = 0; i < next.entries.size(); i++) { 
	                //����λ��ʼ��ӵ�ĩβ 
	                entries.add(next.entries.get(i)); 
	            } 
	            next.parent = null; 
	            next.entries = null;
	            parent.children.remove(next); 
	            //�������� 
	            if (next.next != null) { 
	                BplusNode temp = next; 
	                temp.next.previous = this; 
	                next = temp.next; 
	                temp.previous = null; 
	                temp.next = null; 
	            }else { 
	                next.previous = null; 
	                next = null; 
	            } 
	            //���¸��ڵ�Ĺؼ����б�
	            parent.entries.remove(parent.children.indexOf(this));
                if((!parent.isRoot && (parent.children.size() >= tree.getOrder() / 2
                			&& parent.children.size() >= 2))
                			||parent.isRoot && parent.children.size() >= 2){
	                 return returnStringalue;
				}
	            parent.updateRemove(tree);
                return returnStringalue;
	        } 
        }
        /*�������Ҷ�ӽڵ�*/
        
        //���keyС�ڵ��ڽڵ�����ߵ�key���ص�һ���ӽڵ�������� 
        if (key.compareTo(entries.get(0).getKey()) < 0) { 
            return children.get(0).remove(key, tree); 
        //���key���ڽڵ����ұߵ�key�������һ���ӽڵ�������� 
        }else if (key.compareTo(entries.get(entries.size()-1).getKey()) >= 0) { 
            return children.get(children.size()-1).remove(key, tree); 
        //�����ر�key���ǰһ���ӽڵ�������� 
        }else { 
            int low = 0, high = entries.size() - 1, mid= 0;
            int comp ;
        		while (low <= high) {
        			mid = (low + high) / 2;
        			comp = entries.get(mid).getKey().compareTo(key);
        			if (comp == 0) {
        				return children.get(mid + 1).remove(key, tree); 
        			} else if (comp < 0) {
        				low = mid + 1;
        			} else {
        				high = mid - 1;
        			}
        		}
        		return children.get(low).remove(key, tree); 
        } 
    } 
     
    /** �жϵ�ǰ�ڵ��Ƿ�����ùؼ���*/ 
    protected int contains(Integer key) { 
    		int low = 0, high = entries.size() - 1, mid;
        int comp ;
		while (low <= high) {
			mid = (low + high) / 2;
			comp = entries.get(mid).getKey().compareTo(key);
			if (comp == 0) {
				return mid;
			} else if (comp < 0) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return -1;
    } 
     
    /** ���뵽��ǰ�ڵ�Ĺؼ�����*/ 
    protected void insertOrUpdate(Integer key, String value){ 
    		//������ң�����
        int low = 0, high = entries.size() - 1, mid;
        int comp ;
		while (low <= high) {
			mid = (low + high) / 2;
			comp = entries.get(mid).getKey().compareTo(key);
			if (comp == 0) {
				entries.get(mid).setValue(value); 
				break;
			} else if (comp < 0) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		if(low>high){
			entries.add(low, new SimpleEntry<Integer, String>(key, value));
		}
    } 
     
    /** ɾ���ڵ�*/ 
    protected String remove(Integer key){ 
    		int low = 0,high = entries.size() -1,mid;
    		int comp;
    		while(low<= high){
    			mid  = (low+high)/2;
    			comp = entries.get(mid).getKey().compareTo(key);
    			if(comp == 0){
    				return entries.remove(mid).getValue();
    			}else if(comp < 0){
				low = mid + 1;
			}else {
				high = mid - 1;
			}
    		}
        return null;
    }   
}
