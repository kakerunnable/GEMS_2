/*    */ package ui;
/*    */ 
/*    */ import org.eclipse.jface.resource.ImageDescriptor;
/*    */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*    */ import org.osgi.framework.BundleContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Activator
/*    */   extends AbstractUIPlugin
/*    */ {
/*    */   public static final String PLUGIN_ID = "UI";
/*    */   private static Activator plugin;
/*    */   
/*    */   public void start(BundleContext context) throws Exception {
/* 29 */     super.start(context);
/* 30 */     plugin = this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop(BundleContext context) throws Exception {
/* 38 */     plugin = null;
/* 39 */     super.stop(context);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Activator getDefault() {
/* 48 */     return plugin;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ImageDescriptor getImageDescriptor(String path) {
/* 59 */     return imageDescriptorFromPlugin("UI", path);
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/ui/Activator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */