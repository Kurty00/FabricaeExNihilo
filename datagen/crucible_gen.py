from common_datagen import *

crucibles = {
    'fabricaeexnihilo:oak_crucible': 'minecraft:oak_log',
    'fabricaeexnihilo:spruce_crucible': 'minecraft:spruce_log',
    'fabricaeexnihilo:birch_crucible': 'minecraft:birch_log',
    'fabricaeexnihilo:jungle_crucible': 'minecraft:jungle_log',
    'fabricaeexnihilo:acacia_crucible': 'minecraft:acacia_log',
    'fabricaeexnihilo:dark_oak_crucible': 'minecraft:dark_oak_log',
    'fabricaeexnihilo:porcelain_crucible': 'minecraft:stone'
}


def generate():
    for crucible, material in crucibles.items():
        material_namespace, material_path = material.split(':')
        crucible_namespace, crucible_path = crucible.split(':')
        save_block_model(crucible_path, 'fabricaeexnihilo:block/crucible',
                         {'all': f'{material_namespace}:block/{material_path}'})
        save_blockstate(crucible_path, f'{crucible_namespace}:block/{crucible_path}')
        save_block_item_model(crucible_path, f'{crucible_namespace}:block/{crucible_path}')