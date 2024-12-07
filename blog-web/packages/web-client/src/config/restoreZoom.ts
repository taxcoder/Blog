export default () => {
  //@ts-ignore
  HTMLElement.prototype.getBoundingClientRectBak = HTMLElement.prototype.getBoundingClientRect;

  // @ts-ignore
  HTMLElement.prototype.getBoundingClientRect = function (): DOMRect {
    //@ts-ignore
    const rect = HTMLElement.prototype.getBoundingClientRectBak.call(this);
    // 如果是html元素，直接返回
    if (this === document.documentElement) {
      return rect;
    }
    //@ts-ignore
    let zoom = window.getComputedStyle(document.body).zoom;
    let scrollTop = document.documentElement.scrollTop;
    let scrollLeft = document.documentElement.scrollLeft;
    let offsetScrollTop = scrollTop - scrollTop / zoom;
    let offsetScrollLeft = scrollLeft - scrollLeft / zoom;
    // 重新校准被zoom修改后的浏览器位置
    return new DOMRect(rect.x - offsetScrollLeft, rect.y - offsetScrollTop, rect.width, rect.height);
  };
};
