import { SubscriptionUtil } from './subscription-util';

export function AutoUnsubscribe(constructor: any) {
  const original = constructor.prototype.ngOnDestroy;

  constructor.prototype.ngOnDestroy = function () {
    const subs = this['subs'];
    if (subs instanceof SubscriptionUtil) {
      subs.clear();
    }
    original && typeof original === 'function' && original.apply(this, arguments);
  };
}

export interface AutoUnsubscribable {
  subs: SubscriptionUtil;
}
