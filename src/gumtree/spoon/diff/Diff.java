package gumtree.spoon.diff;

import gumtree.spoon.diff.operations.Operation;
import gumtree.spoon.diff.operations.OperationKind;
import java.util.List;
import spoon.reflect.declaration.CtElement;

public interface Diff {
  List<Operation> getAllOperations();
  
  List<Operation> getRootOperations();
  
  List<Operation> getOperationChildren(Operation paramOperation, List<Operation> paramList);
  
  CtElement changedNode();
  
  CtElement changedNode(Class<? extends Operation> paramClass);
  
  CtElement commonAncestor();
  
  boolean containsOperation(OperationKind paramOperationKind, String paramString);
  
  boolean containsOperation(OperationKind paramOperationKind, String paramString1, String paramString2);
  
  boolean containsOperations(List<Operation> paramList, OperationKind paramOperationKind, String paramString1, String paramString2);
  
  void debugInformation();
}


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/lib/gumtree/spoon/diff/Diff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */