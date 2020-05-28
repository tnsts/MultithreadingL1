package mutithreading.lab1;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class DoubleList implements List<Ammunition>{
	
	class Node {
	    Node next;
	    Node prev;
	    Ammunition ammunition;
	    
	    Node(Node next, Node prev, Ammunition ammunition){
	    	this.next = next;
	    	this.prev = prev;
	    	this.ammunition = ammunition;
	    }
	    
	    Node(Ammunition ammunition){
	    	this.ammunition = ammunition;
	    }

		public Node() {}
	}
	
	private int size;
	private Node first;    
    private Node last;       
 
    DoubleList(){
    	size = 0;
    	first = null;
    	last = null;
    }
    
    public int size() {
    	return size;
    }
 
    public void printList() {
    	Node curr = first;         
        while (curr != null) {
            curr.ammunition.showFeatures(); 
            curr = curr.next; 
            System.out.println();
        }
    }
    
    synchronized private void waitUtilSuccess(int index) {
    	while (!(index >= 0 && index < size)) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Caught Interrupted Exeption while waitUntilSuccess function");
				e.printStackTrace();
			}
		}
    }
    
	@Override
	public boolean isEmpty() {
		if (size == 0) return true;
		return false;
	}

	@Override
	public boolean contains(Object o) {
		Node tmp = first;  
        while (tmp != null) {
        	if (o.equals(tmp.ammunition))return true;
        	tmp = tmp.next;
        }
        return false;
	}

	@Override
	public Iterator<Ammunition> iterator() {
		return new Iterator <Ammunition>() {
			public Node current = new Node(first, null, null);
	
			@Override
			public void remove() {
				DoubleList.this.remove(current.ammunition);
			}
	
			@Override
			public Ammunition next() {
				if (hasNext()) {
					current = current.next;
					return current.ammunition;
				} else return null;
			}
	
			@Override
			public boolean hasNext() {
				return current.next != null;
			}
		};
	}
	
	@Override
	public Object[] toArray() {
		Ammunition[] result = new Ammunition[size];
        int i = 0;
        for (Node x = first; x != null; x = x.next) {
        	result[i++] = x.ammunition;
        }
        return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		 if (a.length < size) {
			 a = (T[])Array.newInstance(a.getClass().getComponentType(), size);
		 }
		
        int i = 0;
        Object[] result = a;
        for (Node x = first; x != null; x = x.next) {
        	result[i++] = x.ammunition;
        }

        if (a.length > size) {
        	for (i = size; i < a.length; i++) {
        		a[i] = null;
        	}
        }
        return a;
	}

	@Override
	synchronized public boolean add(Ammunition ammunition) {
		Node a = new Node(ammunition);
        
        if (last == null) {                           
        	first = a;               
        	last = a;
        } else {
        	last.next = a;
            a.prev = last;
            last = a;              
        }
        
        size++;
        
        notifyAll();
        
        return true;
	}

	@Override
	 public boolean remove(Object o) {
		if (o == null) {
            for (Node tmp = first; tmp != null; tmp = tmp.next) {
                if (tmp.ammunition == null) {
                	Node next = tmp.next;
            		Node prev = tmp.prev;

            		if (prev == null) first = next;
            		else {
            			prev.next = next;
            			tmp.prev = null;
            		}
            		
            		if (next == null) last = prev;
            		else {
            			next.prev = prev;
            			tmp.next = null;
            		}
            		
            		tmp.ammunition = null;
            		size--;
            		return true;
                }
            }
        } else {
            for (Node tmp = first; tmp != null; tmp = tmp.next) {
                if (o.equals(tmp.ammunition)) {
                	Node next = tmp.next;
            		Node prev = tmp.prev;

            		if (prev == null) first = next;
            		else {
            			prev.next = next;
            			tmp.prev = null;
            		}
            		
            		if (next == null) last = prev;
            		else {
            			next.prev = prev;
            			tmp.next = null;
            		}
            		
            		tmp.ammunition = null;
            		size--;
            		return true;
                }
            }
        }
        return false;	
	    }
	
	@Override
	public boolean containsAll(Collection<?> c) {
		Object[] a = c.toArray();
        if (a.length == 0) return false;
        
        boolean is = true;
        for (Object o : a) {
            is = is && contains(o);
        }
        return is;
	}

	@Override
	public boolean addAll(Collection<? extends Ammunition> c) {
		return addAll(size, c);
	}

	@Override
	synchronized public boolean addAll(int index, Collection<? extends Ammunition> c) {
//		if (!(index >= 0 && index <= size))
//			throw new IndexOutOfBoundsException("Out of Bounds");

		waitUtilSuccess(index);
		
		 Object[] a = c.toArray();
	     int numNew = a.length;
	     if (numNew == 0) return false;

	     Node prev, curr;
	     if (index == size) {
	    	 curr = null;
	    	 prev = last;
	    } else {
	    	 curr = node(index);
	         prev = curr.prev;
	     }

	     for (Object o : a) {
	    	 Ammunition e = (Ammunition) o;
	         Node newNode = new Node(null, prev, e);
	         if (prev == null) first = newNode;
	         else prev.next = newNode;
	         prev = newNode;
	     }

	     if (curr == null) last = prev;
	     else {
	    	 prev.next = curr;
	         curr.prev = prev;
	     }

	     size += numNew;
	     return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Object[] a = c.toArray();
        if (a.length == 0) return false;
        for (Object o : a) {
            remove(o);
        }
        return true;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
	    for (int i = size() - 1; i >= 0; i--) {
	        Object obj = get(i);
	        if (!c.contains(obj)) {
	            remove(i);
	            changed = true;
	        }
	    }
	    return changed;
	}

	@Override
	public void clear() {
		Node tmp = first;  
        while (tmp != null) {
	        Node next = tmp.next;
	        tmp.ammunition = null;
	        tmp.next = null;
	        tmp.prev = null;
	        tmp = next;
	    }
	    first = last = null;
	    size = 0;
	}
	
	 Node node(int index) {
	        if (index < (size >> 1)) {
	            Node x = first;
	            for (int i = 0; i < index; i++)
	                x = x.next;
	            return x;
	        } else {
	            Node x = last;
	            for (int i = size - 1; i > index; i--)
	                x = x.prev;
	            return x;
	        }
	    }

	@Override
	synchronized public Ammunition get(int index) {
//		if (!(index >= 0 && index < size))
//			throw new IndexOutOfBoundsException("Out of Bounds");
		
		waitUtilSuccess(index);
		
		return node(index).ammunition;
	}

	@Override
	synchronized public Ammunition set(int index, Ammunition element) {
//		if (!(index >= 0 && index < size))
//			throw new IndexOutOfBoundsException("Out of Bounds");
			
		waitUtilSuccess(index);
		
        Node x = node(index);
        Ammunition oldVal = x.ammunition;
        x.ammunition = element;
        return oldVal;
	}

	@Override
	synchronized public void add(int index, Ammunition element) {
//		if (!(index >= 0 && index < size))
//			throw new IndexOutOfBoundsException("Out of Bounds");
		
		waitUtilSuccess(index);
		
		if (index == size) add(element);
        else {
        	Node succ = node(index);
        	final Node pred = succ.prev;
            final Node newNode = new Node(succ, pred, element);
            succ.prev = newNode;
            if (pred == null) first = newNode;
            else pred.next = newNode;
            size++;
        }	
	}

	@Override
	synchronized public Ammunition remove(int index) {
//		if (!(index >= 0 && index < size))
//			throw new IndexOutOfBoundsException("Out of Bounds");
		
		waitUtilSuccess(index);
		
		Ammunition rm = node(index).ammunition;
		if (remove(rm)) return rm;
		return null;
	}

	@Override
	public int indexOf(Object o) {
		 int index = 0;
	        if (o == null) {
	            for (Node x = first; x != null; x = x.next) {
	                if (x.ammunition == null)
	                    return index;
	                index++;
	            }
	        } else {
	            for (Node x = first; x != null; x = x.next) {
	                if (o.equals(x.ammunition))
	                    return index;
	                index++;
	            }
	        }
	        return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		int index = size;
        if (o == null) {
            for (Node x = last; x != null; x = x.prev) {
                index--;
                if (x.ammunition == null)
                    return index;
            }
        } else {
            for (Node x = last; x != null; x = x.prev) {
                index--;
                if (o.equals(x.ammunition))
                    return index;
            }
        }
        return -1;
	}

	@Override
	public ListIterator<Ammunition> listIterator() {
		return new ListIterator<Ammunition>() {

			public Node current = new Node(first, null, null);
			
			@Override
			public void remove() {
				DoubleList.this.remove(current.ammunition);
			}
	
			@Override
			public Ammunition next() {
				if (hasNext()) {
					current = current.next;
					return current.ammunition;
				} else return null;
			}
	
			@Override
			public boolean hasNext() {
				return current.next != null;
			}

			@Override
			public boolean hasPrevious() {
				return current.next != null;
			}

			@Override
			public Ammunition previous() {
				return current.prev.ammunition;
			}

			@Override
			public int nextIndex() {
				return indexOf(current.next.ammunition);
			}

			@Override
			public int previousIndex() {
				return indexOf(current.prev.ammunition);
			}

			@Override
			public void set(Ammunition e) {
				current.ammunition = e;
			}

			@Override
			public void add(Ammunition e) {
				DoubleList.this.add(indexOf(current.ammunition) + 1, e);
			}
			
		};
	}

	@Override
	public ListIterator<Ammunition> listIterator(int index) {
		ListIterator<Ammunition> iter = this.listIterator();
			while (iter.hasNext() && iter.nextIndex() <= index) {
				iter.next();
			}
			return iter;
	}
	
	@Override
	public List<Ammunition> subList(int fromIndex, int toIndex) {
		LinkedList<Ammunition> sub = new LinkedList<Ammunition>();
		for (int i = fromIndex; i < toIndex; i++) {
			sub.add(node(i).ammunition);
		}
		return sub;
	}
}
