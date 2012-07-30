package org.jnario.ui.quickfix;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.xtend.core.jvmmodel.IXtendJvmAssociations;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.jdt.IJavaElementFinder;
import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.XAbstractFeatureCall;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XMemberFeatureCall;
import org.eclipse.xtext.xbase.resource.XbaseResource;
import org.eclipse.xtext.xbase.typing.ITypeProvider;
import org.jnario.ui.quickfix.CreateJavaMethod;
import org.jnario.ui.quickfix.CreateXtendMethod;
import org.jnario.ui.quickfix.JavaMethodBuilder;
import org.jnario.ui.quickfix.MethodBuilderProvider;
import org.jnario.ui.quickfix.XtendMethodBuilder;

@SuppressWarnings("all")
public class CreateMissingMethod {
  private IJavaElementFinder elementProvider;
  
  private ITypeProvider typeProvider;
  
  private IXtendJvmAssociations jvmAssociations;
  
  private MethodBuilderProvider methodBuilderProvider;
  
  @Inject
  public CreateMissingMethod(final IJavaElementFinder elementProvider, final ITypeProvider typeProvider, final IXtendJvmAssociations jvmAssociations, final MethodBuilderProvider methodBuilderProvider) {
    this.elementProvider = elementProvider;
    this.typeProvider = typeProvider;
    this.jvmAssociations = jvmAssociations;
    this.methodBuilderProvider = methodBuilderProvider;
  }
  
  public void apply(final Issue issue, final IssueResolutionAcceptor issueResolutionAcceptor, final XMemberFeatureCall featureCall, final String issueString) {
    boolean _hasMissingMethod = this.hasMissingMethod(featureCall);
    boolean _not = (!_hasMissingMethod);
    if (_not) {
      return;
    }
    boolean _receiverIsReadOnly = this.receiverIsReadOnly(featureCall);
    if (_receiverIsReadOnly) {
      return;
    }
    final IModification modification = this.createModification(featureCall, issueString);
    String _plus = ("create Method \'" + issueString);
    String _plus_1 = (_plus + "\' in ");
    JvmType _targetType = this.targetType(featureCall);
    String _simpleName = _targetType==null?(String)null:_targetType.getSimpleName();
    String _plus_2 = (_plus_1 + _simpleName);
    issueResolutionAcceptor.accept(issue, _plus_2, "", null, modification);
  }
  
  public boolean hasMissingMethod(final XMemberFeatureCall featureCall) {
    boolean _xblockexpression = false;
    {
      JvmType _targetType = this.targetType(featureCall);
      boolean _isUnknown = this.isUnknown(_targetType);
      if (_isUnknown) {
        return false;
      }
      JvmIdentifiableElement _feature = featureCall.getFeature();
      boolean _eIsProxy = _feature==null?false:_feature.eIsProxy();
      _xblockexpression = (_eIsProxy);
    }
    return Boolean.valueOf(_xblockexpression);
  }
  
  public boolean receiverIsReadOnly(final XMemberFeatureCall call) {
    IJavaElement _targetJavaElement = this.targetJavaElement(call);
    return _targetJavaElement.isReadOnly();
  }
  
  public IModification createModification(final XMemberFeatureCall call, final String methodName) {
    boolean _isXtendClass = this.isXtendClass(call);
    if (_isXtendClass) {
      final XtendMethodBuilder methodBuilder = this.methodBuilderProvider.newXtendMethodBuilder(methodName);
      this.configureWith(methodBuilder, call);
      CreateXtendMethod _createXtendMethod = new CreateXtendMethod(methodBuilder);
      return _createXtendMethod;
    } else {
      final JavaMethodBuilder methodBuilder_1 = this.methodBuilderProvider.newJavaMethodBuilder(methodName);
      this.configureWith(methodBuilder_1, call);
      IJavaElement _targetJavaElement = this.targetJavaElement(call);
      CreateJavaMethod _createJavaMethod = new CreateJavaMethod(methodBuilder_1, ((IType) _targetJavaElement));
      return _createJavaMethod;
    }
  }
  
  private void configureWith(final XtendMethodBuilder methodBuilder, final XMemberFeatureCall call) {
    methodBuilder.setFeatureCall(call);
  }
  
  private IJavaElement targetJavaElement(final XMemberFeatureCall call) {
    JvmType _targetType = this.targetType(call);
    IJavaElement _findElementFor = this.elementProvider.findElementFor(_targetType);
    return _findElementFor;
  }
  
  private boolean isXtendClass(final XMemberFeatureCall call) {
    JvmType _targetType = this.targetType(call);
    Resource _eResource = _targetType.eResource();
    return (_eResource instanceof XbaseResource);
  }
  
  private JvmType targetType(final XMemberFeatureCall featureCall) {
    JvmIdentifiableElement _targetFeature = this.targetFeature(featureCall);
    JvmTypeReference _typeForIdentifiable = _targetFeature==null?(JvmTypeReference)null:this.typeProvider.getTypeForIdentifiable(_targetFeature);
    JvmType _type = _typeForIdentifiable==null?(JvmType)null:_typeForIdentifiable.getType();
    return _type;
  }
  
  private JvmIdentifiableElement targetFeature(final XMemberFeatureCall featureCall) {
    JvmIdentifiableElement _xblockexpression = null;
    {
      final XExpression memberCallTarget = featureCall.getMemberCallTarget();
      boolean _not = (!(memberCallTarget instanceof XAbstractFeatureCall));
      if (_not) {
        return null;
      }
      JvmIdentifiableElement _feature = ((XAbstractFeatureCall) memberCallTarget).getFeature();
      _xblockexpression = (_feature);
    }
    return _xblockexpression;
  }
  
  private boolean isUnknown(final EObject obj) {
    boolean _or = false;
    boolean _equals = Objects.equal(obj, null);
    if (_equals) {
      _or = true;
    } else {
      boolean _eIsProxy = obj.eIsProxy();
      _or = (_equals || _eIsProxy);
    }
    return _or;
  }
}