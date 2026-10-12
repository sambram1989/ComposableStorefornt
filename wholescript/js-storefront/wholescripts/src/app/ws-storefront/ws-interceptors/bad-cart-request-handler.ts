import { HttpErrorResponse, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { isCartError, isCartNotFoundError } from "@spartacus/cart/base/core";
import { HttpErrorHandler, HttpResponseStatus, Priority, GlobalMessageType, ErrorModel } from "@spartacus/core";


@Injectable({
  providedIn: 'root',
})
export class CustomBadCartRequestHandler extends HttpErrorHandler {
  override responseStatus = HttpResponseStatus.BAD_REQUEST;

  getPriority(): Priority {
    return Priority.NORMAL;
  }

  override hasMatch(errorResponse: HttpErrorResponse): boolean {
    return (
      super.hasMatch(errorResponse) &&
      this.getErrors(errorResponse).some(isCartError)
    );
  }

  handleError(request: HttpRequest<any>, response: HttpErrorResponse): void {
    this.handleCartNotFoundError(request, response);
    this.handleOtherCartErrors(request, response);
  }

  protected handleCartNotFoundError(
    _request: HttpRequest<any>,
    response: HttpErrorResponse
  ) {
    return; // Suppressed the 'cart not found' error
  }

  protected handleOtherCartErrors(
    _request: HttpRequest<any>,
    response: HttpErrorResponse
  ): void {
    this.getErrors(response)
      .filter((e) => e.reason !== 'notFound' || e.subjectType !== 'cart')
      .forEach((error) => {
        this.globalMessageService.add(
          error.message
            ? error.message
            : { key: 'httpHandlers.otherCartErrors' },
          GlobalMessageType.MSG_TYPE_ERROR
        );
      });
  }

  protected getErrors(response: HttpErrorResponse): ErrorModel[] {
    return (response.error?.errors || []).filter(
      (error: any) => error.type !== 'JaloObjectNoLongerValidError'
    );
  }
}
