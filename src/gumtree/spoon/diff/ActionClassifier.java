/*    */ package gumtree.spoon.diff;
/*    */ 
/*    */ import com.github.gumtreediff.actions.model.Action;
/*    */ import com.github.gumtreediff.matchers.Mapping;
/*    */ import com.github.gumtreediff.matchers.MappingStore;
/*    */ import com.github.gumtreediff.tree.ITree;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ActionClassifier
/*    */ {
/* 30 */   private Set<ITree> srcUpdTrees = new HashSet<>();
/* 31 */   private Set<ITree> dstUpdTrees = new HashSet<>();
/* 32 */   private Set<ITree> srcMvTrees = new HashSet<>();
/* 33 */   private Set<ITree> dstMvTrees = new HashSet<>();
/* 34 */   private Set<ITree> srcDelTrees = new HashSet<>();
/* 35 */   private Set<ITree> dstAddTrees = new HashSet<>();
/* 36 */   private Map<ITree, Action> originalActionsSrc = new HashMap<>();
/* 37 */   private Map<ITree, Action> originalActionsDst = new HashMap<>();
/*    */   
/*    */   List<Action> getRootActions(Set<Mapping> rawMappings, List<Action> actions) {
/* 40 */     clean();
/* 41 */     MappingStore mappings = new MappingStore(rawMappings);
/* 42 */     for (Action action : actions) {
/* 43 */       ITree original = action.getNode();
/* 44 */       if (action instanceof com.github.gumtreediff.actions.model.Delete) {
/* 45 */         this.srcDelTrees.add(original);
/* 46 */         this.originalActionsSrc.put(original, action); continue;
/* 47 */       }  if (action instanceof com.github.gumtreediff.actions.model.Insert) {
/* 48 */         this.dstAddTrees.add(original);
/* 49 */         this.originalActionsDst.put(original, action); continue;
/* 50 */       }  if (action instanceof com.github.gumtreediff.actions.model.Update) {
/* 51 */         ITree dest = mappings.getDst(original);
/* 52 */         original.setMetadata("spoon_object_dest", dest.getMetadata("spoon_object"));
/* 53 */         this.srcUpdTrees.add(original);
/* 54 */         this.dstUpdTrees.add(dest);
/* 55 */         this.originalActionsSrc.put(original, action); continue;
/* 56 */       }  if (action instanceof com.github.gumtreediff.actions.model.Move) {
/* 57 */         ITree dest = mappings.getDst(original);
/* 58 */         original.setMetadata("spoon_object_dest", dest.getMetadata("spoon_object"));
/* 59 */         this.srcMvTrees.add(original);
/* 60 */         this.dstMvTrees.add(dest);
/* 61 */         this.originalActionsDst.put(dest, action);
/*    */       } 
/*    */     } 
/* 64 */     return getRootActions();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private List<Action> getRootActions() {
/* 71 */     List<Action> rootActions = (List<Action>)this.srcUpdTrees.stream().map(t -> (Action)this.originalActionsSrc.get(t)).collect(Collectors.toList());
/* 72 */     rootActions.addAll((Collection<? extends Action>)this.srcDelTrees.stream()
/* 73 */         .filter(t -> (!this.srcDelTrees.contains(t.getParent()) && !this.srcUpdTrees.contains(t.getParent())))
/* 74 */         .map(t -> (Action)this.originalActionsSrc.get(t))
/* 75 */         .collect(Collectors.toList()));
/* 76 */     rootActions.addAll((Collection<? extends Action>)this.dstAddTrees.stream()
/* 77 */         .filter(t -> (!this.dstAddTrees.contains(t.getParent()) && !this.dstUpdTrees.contains(t.getParent())))
/* 78 */         .map(t -> (Action)this.originalActionsDst.get(t))
/* 79 */         .collect(Collectors.toList()));
/* 80 */     rootActions.addAll((Collection<? extends Action>)this.dstMvTrees.stream()
/* 81 */         .filter(t -> !this.dstMvTrees.contains(t.getParent()))
/* 82 */         .map(t -> (Action)this.originalActionsDst.get(t))
/* 83 */         .collect(Collectors.toList()));
/* 84 */     rootActions.removeAll(Collections.singleton(null));
/* 85 */     return rootActions;
/*    */   }
/*    */   
/*    */   private void clean() {
/* 89 */     this.srcUpdTrees.clear();
/* 90 */     this.dstUpdTrees.clear();
/* 91 */     this.srcMvTrees.clear();
/* 92 */     this.dstMvTrees.clear();
/* 93 */     this.srcDelTrees.clear();
/* 94 */     this.dstAddTrees.clear();
/* 95 */     this.originalActionsSrc.clear();
/* 96 */     this.originalActionsDst.clear();
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/lib/gumtree/spoon/diff/ActionClassifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */