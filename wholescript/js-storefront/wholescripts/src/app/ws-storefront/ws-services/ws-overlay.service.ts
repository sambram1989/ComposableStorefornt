import { FocusTrapFactory } from '@angular/cdk/a11y';
import { Overlay, OverlayConfig, OverlayRef } from '@angular/cdk/overlay';
import { TemplatePortal } from '@angular/cdk/portal';
import { inject, Injectable, Renderer2, RendererFactory2 } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class WsOverlay {
  private overlay = inject(Overlay);
  private renderer: Renderer2;
  private overlayStack: OverlayRef[] = [];
  prevFocusElm: HTMLElement | null = null;
  constructor(
    rendererFactory: RendererFactory2,
    private focusTrapFactory: FocusTrapFactory
  ) {
    this.renderer = rendererFactory.createRenderer(null, null);
  }
  openModal(
    portal: TemplatePortal<any>,
    width?: string,
    height?: string,
    isCenter?: boolean,
    backdropClickShouldNotClosePopup?: boolean
  ) {
    this.prevFocusElm = document.activeElement as HTMLElement;
    const config = new OverlayConfig({
      positionStrategy: isCenter
        ? this.overlay
            .position()
            .global()
            .centerHorizontally()
            .centerVertically()
        : this.overlay.position().global().right(),
      width: width || '50%',
      height: height || '100vh',
      hasBackdrop: true,
    });
    const overlayRef = this.overlay.create(config);
    this.overlayStack.push(overlayRef);
    overlayRef.attach(portal);

    this.setBodyOverflow(true);

    if (!backdropClickShouldNotClosePopup)
      overlayRef.backdropClick().subscribe(() => this.closeTopModal());
    this.focusFirstItemInDialog(overlayRef);
  }

  focusFirstItemInDialog(overlayRef: OverlayRef) {
    const focusTrap = this.focusTrapFactory.create(overlayRef.overlayElement);

    focusTrap.focusInitialElement();
  }

  closeTopModal() {
    if (this.overlayStack.length > 0) {
      const overlayRef = this.overlayStack.pop();
      if (overlayRef) {
        overlayRef.detach();
        if (this.overlayStack.length === 0) {
          this.setBodyOverflow(false);
        }
      }
    }
    if (this.overlayStack.length === 0) {
      if (this.prevFocusElm) {
        this.prevFocusElm.focus();
        this.prevFocusElm = null;
      }
    }
  }

  closeAllModals() {
    while (this.overlayStack.length > 0) {
      const overlayRef = this.overlayStack.pop();
      if (overlayRef) {
        overlayRef.detach();
      }
    }
    this.setBodyOverflow(false);
  }

  setBodyOverflow(isHidden: boolean) {
    if (isHidden) {
      this.renderer.setStyle(document.body, 'overflow', 'hidden');
    } else {
      this.renderer.removeStyle(document.body, 'overflow');
    }
  }
}
