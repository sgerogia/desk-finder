import cv2 as cv
import numpy as np
from matplotlib import pyplot as plt

def process_image():

    img_rgb = cv.imread('./floors/floor.png')
    img_gray = cv.cvtColor(img_rgb, cv.COLOR_BGR2GRAY)

    search_template(img_rgb, img_gray, './templates/desk_1.png', (0, 0, 255))
    search_template(img_rgb, img_gray, './templates/desk_2.png', (0, 255, 0))
    search_template(img_rgb, img_gray, './templates/desk_3.png', (255, 0, 0))
    search_template(img_rgb, img_gray, './templates/desk_4.png', (100, 0, 255))

    cv.imwrite('./output/1st_floor_output.png', img_rgb)


def search_template(floor_image_rgb, floor_image_gray, template_name, color):
    template = cv.imread(template_name, 0)
    w, h = template.shape[::-1]

    res = cv.matchTemplate(floor_image_gray, template, cv.TM_CCOEFF_NORMED)
    threshold = 0.5
    loc = np.where(res >= threshold)

    desk_count = 0

    mask = np.zeros(floor_image_rgb.shape[:2], np.uint8)

    for pt in zip(*loc[::-1]):
        if mask[pt[1] + h/2, pt[0] + w/2] != 255:
            mask[pt[1]:pt[1]+h, pt[0]:pt[0]+w] = 255
            cv.rectangle(floor_image_rgb, pt, (pt[0] + w, pt[1] + h), color, 1)

#            print('\t<a xlink:href="./action?id={}">'.format(pt))
#            print('\t\t<rect id="{}" fill="#00D300" opacity="0.2" x="{}" y="{}" width="{}" height="{}"/>'.format(p_to_s(pt), pt[0], pt[1], w, h))
#            print('\t</a>')

            print('document.getElementById("{}").addEventListener("onmouseover", function(e) {{ getDetails("{}") }});'.format(p_to_s(pt), p_to_s(pt)))

#           or

#            <map name="mapname">
#            print('<area shape="rect" coords="{}" href="http://en.wikipedia.org/" onmouseover="getDetails("{}")">')
#            </map>


            desk_count += 1

    print("Found {} desks, color {}".format(desk_count, color))

def p_to_s(point):
    return '{}_{}'.format(point[0], point[1])

if __name__ == "__main__":
    process_image()
