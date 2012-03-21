package org.jnario.feature.naming;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.jnario.feature.feature.Step;
import org.jnario.feature.feature.StepExpression;
import org.jnario.feature.feature.StepReference;

/**
 * @author Sebastian Benz - Initial contribution and API
 */
@SuppressWarnings("all")
public class StepExpressionProvider {
  public StepExpression expressionOf(final Step step) {
    if ((step instanceof StepReference)) {
      this.getOrCreateExpression(((StepReference) step));
    }
    return step.getStepExpression();
  }
  
  public StepExpression getOrCreateExpression(final StepReference ref) {
    Step _reference = ref==null?(Step)null:ref.getReference();
    StepExpression _stepExpression = _reference==null?(StepExpression)null:_reference.getStepExpression();
    EObject _copy = EcoreUtil.copy(_stepExpression);
    final StepExpression expr = ((StepExpression) _copy);
    ref.setStepExpression(expr);
    return expr;
  }
}
